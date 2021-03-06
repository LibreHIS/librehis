//#############################################################################
//#                                                                           #
//#  Copyright (C) <2015>  <IMS MAXIMS>                                       #
//#                                                                           #
//#  This program is free software: you can redistribute it and/or modify     #
//#  it under the terms of the GNU Affero General Public License as           #
//#  published by the Free Software Foundation, either version 3 of the       #
//#  License, or (at your option) any later version.                          # 
//#                                                                           #
//#  This program is distributed in the hope that it will be useful,          #
//#  but WITHOUT ANY WARRANTY; without even the implied warranty of           #
//#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            #
//#  GNU Affero General Public License for more details.                      #
//#                                                                           #
//#  You should have received a copy of the GNU Affero General Public License #
//#  along with this program.  If not, see <http://www.gnu.org/licenses/>.    #
//#                                                                           #
//#  IMS MAXIMS provides absolutely NO GUARANTEE OF THE CLINICAL SAFTEY of    #
//#  this program.  Users of this software do so entirely at their own risk.  #
//#  IMS MAXIMS only ensures the Clinical Safety of unaltered run-time        #
//#  software that it builds, deploys and maintains.                          #
//#                                                                           #
//#############################################################################
//#EOH
// This code was generated by Barbara Worwood using IMS Development Environment (version 1.80 build 5589.25814)
// Copyright (C) 1995-2015 IMS MAXIMS. All rights reserved.
// WARNING: DO NOT MODIFY the content of this file

package ims.core.forms.casenotearchiveordestroy;

import ims.framework.delegates.*;

abstract public class Handlers implements ims.framework.UILogic, IFormUILogicCode
{
	abstract protected void bindcmbTypeArchLookup();
	abstract protected void defaultcmbTypeArchLookupValue();
	abstract protected void bindcmbStatusArchLookup();
	abstract protected void defaultcmbStatusArchLookupValue();
	abstract protected void bindcmbTypeDestrLookup();
	abstract protected void defaultcmbTypeDestrLookupValue();
	abstract protected void bindcmbStatusDestrLookup();
	abstract protected void defaultcmbStatusDestrLookupValue();
	abstract protected void bindcmbScannedTypeLookup();
	abstract protected void defaultcmbScannedTypeLookupValue();
	abstract protected void bindcmbScannedStatusLookup();
	abstract protected void defaultcmbScannedStatusLookupValue();
	abstract protected void onFormModeChanged();
	abstract protected void onFormOpen(Object[] args) throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onBtnCancelClick() throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void oncmbTypeArchValueSet(Object value);
	abstract protected void oncmbStatusArchValueSet(Object value);
	abstract protected void oncmbTypeDestrValueSet(Object value);
	abstract protected void oncmbStatusDestrValueSet(Object value);
	abstract protected void oncmbScannedTypeValueSet(Object value);
	abstract protected void oncmbScannedStatusValueSet(Object value);
	abstract protected void onBtnActionClick() throws ims.framework.exceptions.PresentationLogicException;

	public final void setContext(ims.framework.UIEngine engine, GenForm form)
	{
		this.engine = engine;
		this.form = form;

		this.form.setFormModeChangedEvent(new FormModeChanged()
		{
			private static final long serialVersionUID = 1L;
			public void handle()
			{
				onFormModeChanged();
			}
		});
		this.form.setFormOpenEvent(new FormOpen()
		{
			private static final long serialVersionUID = 1L;
			public void handle(Object[] args) throws ims.framework.exceptions.PresentationLogicException
			{
				bindLookups();
				onFormOpen(args);
			}
		});
		this.form.btnCancel().setClickEvent(new Click()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onBtnCancelClick();
			}
		});
		this.form.lyrArchDestr().tabPageArchive().cmbTypeArch().setValueSetEvent(new ComboBoxValueSet()
		{
			private static final long serialVersionUID = 1L;
			public void handle(Object value)
			{
				oncmbTypeArchValueSet(value);
			}
		});
		this.form.lyrArchDestr().tabPageArchive().cmbStatusArch().setValueSetEvent(new ComboBoxValueSet()
		{
			private static final long serialVersionUID = 1L;
			public void handle(Object value)
			{
				oncmbStatusArchValueSet(value);
			}
		});
		this.form.lyrArchDestr().tabPageDestroy().cmbTypeDestr().setValueSetEvent(new ComboBoxValueSet()
		{
			private static final long serialVersionUID = 1L;
			public void handle(Object value)
			{
				oncmbTypeDestrValueSet(value);
			}
		});
		this.form.lyrArchDestr().tabPageDestroy().cmbStatusDestr().setValueSetEvent(new ComboBoxValueSet()
		{
			private static final long serialVersionUID = 1L;
			public void handle(Object value)
			{
				oncmbStatusDestrValueSet(value);
			}
		});
		this.form.lyrArchDestr().tabPageScan().cmbScannedType().setValueSetEvent(new ComboBoxValueSet()
		{
			private static final long serialVersionUID = 1L;
			public void handle(Object value)
			{
				oncmbScannedTypeValueSet(value);
			}
		});
		this.form.lyrArchDestr().tabPageScan().cmbScannedStatus().setValueSetEvent(new ComboBoxValueSet()
		{
			private static final long serialVersionUID = 1L;
			public void handle(Object value)
			{
				oncmbScannedStatusValueSet(value);
			}
		});
		this.form.btnAction().setClickEvent(new Click()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onBtnActionClick();
			}
		});
	}
	protected void bindLookups()
	{
		bindcmbTypeArchLookup();
		bindcmbStatusArchLookup();
		bindcmbTypeDestrLookup();
		bindcmbStatusDestrLookup();
		bindcmbScannedTypeLookup();
		bindcmbScannedStatusLookup();
	}
	protected void rebindAllLookups()
	{
		bindcmbTypeArchLookup();
		bindcmbStatusArchLookup();
		bindcmbTypeDestrLookup();
		bindcmbStatusDestrLookup();
		bindcmbScannedTypeLookup();
		bindcmbScannedStatusLookup();
	}
	protected void defaultAllLookupValues()
	{
		defaultcmbTypeArchLookupValue();
		defaultcmbStatusArchLookupValue();
		defaultcmbTypeDestrLookupValue();
		defaultcmbStatusDestrLookupValue();
		defaultcmbScannedTypeLookupValue();
		defaultcmbScannedStatusLookupValue();
	}

	public void free()
	{
		this.engine = null;
		this.form = null;
	}
	protected ims.framework.UIEngine engine;
	protected GenForm form;
}
