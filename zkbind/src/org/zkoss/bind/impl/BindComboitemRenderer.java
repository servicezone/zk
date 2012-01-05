/* BindComboitemRenderer.java

	Purpose:
		
	Description:
		
	History:
		Aug 17, 2011 3:47:56 PM, Created by henrichen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.bind.impl;

import org.zkoss.lang.Objects;
import org.zkoss.xel.VariableResolverX;
import org.zkoss.xel.XelContext;
import org.zkoss.xel.XelException;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.impl.LoadStatus;

/**
 * comboitem renderer for binding.
 * @author henrichen
 *
 */
public class BindComboitemRenderer extends AbstractRenderer implements ComboitemRenderer<Object>{
	private static final long serialVersionUID = 1463169907348730644L;
	
	public void render(final Comboitem item, final Object data) throws Exception {
		final Combobox cb = (Combobox)item.getParent();
		final Template tm = resoloveTemplate(cb,item,data,item.getIndex(),"model");
		if (tm == null) {
			item.setLabel(Objects.toString(data));
			item.setValue(data);
		} else {
			final String var = (String) tm.getParameters().get(EACH_ATTR);
			final String varnm = var == null ? EACH_VAR : var; //var is not specified, default to "each"
			final String itervar = (String) tm.getParameters().get(STATUS_ATTR);
			final String itervarnm = itervar == null ? var+STATUS_POST_VAR : itervar; //provide default value if not specified
			final Component[] items = tm.create(cb, item,
				new VariableResolverX() {
					public Object resolveVariable(String name) {
						//shall never call here
						return varnm.equals(name) ? data : null;
					}
	
					public Object resolveVariable(XelContext ctx, Object base, Object name) throws XelException {
						if (base == null) {
							if(varnm.equals(name)){
								return data;
							}else if(itervarnm.equals(name)){//iteration status
								return new AbstractIterationStatus(){
									private static final long serialVersionUID = 1L;

									@Override
									public int getIndex() {
										return Integer.valueOf(item.getIndex());
									}
								};
							}
						}
						return null;
					}
				}, null);
			if (items.length != 1)
				throw new UiException("The model template must have exactly one item, not "+items.length);

			final Comboitem nci = (Comboitem)items[0];
			((LoadStatus)(((AbstractComponent)nci).getExtraCtrl())).setIndex(item.getIndex());
			nci.setAttribute(BinderImpl.VAR, varnm); // for the converter to get the value
			nci.setAttribute(varnm, data); //kept the value
			nci.setAttribute(itervarnm, new AbstractIterationStatus(){//provide iteration status in this context
				private static final long serialVersionUID = 1L;

				@Override
				public int getIndex() {
					return Integer.valueOf(nci.getIndex());
				}
			});
			
			if (nci.getValue() == null) //template might set it
				nci.setValue(data);
			item.setAttribute("org.zkoss.zul.model.renderAs", nci);
				//indicate a new item is created to replace the existent one
			item.detach();
			
			//bug #ZK-677: combobox selection is lost after reload model
			//binding Comboitem immediately, @see BindUiLifeCycle#afterComponentAttached
			Events.sendEvent(new Event(BinderImpl.ON_BIND_INIT, nci));
		}
	}
}