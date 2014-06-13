//#############################################################################
//#                                                                           #
//#  Copyright (C) <2014>  <IMS MAXIMS>                                       #
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
//#############################################################################
//#EOH
// This code was generated by Barbara Worwood using IMS Development Environment (version 1.80 build 5007.25751)
// Copyright (C) 1995-2014 IMS MAXIMS. All rights reserved.
// WARNING: DO NOT MODIFY the content of this file

package ims.admin.forms.edconfig;

import ims.framework.delegates.*;

abstract public class Handlers implements ims.framework.UILogic, IFormUILogicCode, ims.framework.interfaces.IClearInfo
{
	abstract protected void onFormModeChanged();
	abstract protected void onFormOpen(Object[] args) throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onBtnCancelClick() throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onBtnSaveClick() throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onBtnEditClick() throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onGrdAttendanceDetailsControlConfigGridCheckBoxClicked(int column, GenForm.lyrEDLayer.tabPageAttendanceDetConfigContainer.lyrAttendanceDetailsLayer.tabPageAttendanceDetalisContainer.grdAttendanceDetailsControlConfigRow row, boolean isChecked) throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onGrdChartRequestGridCheckBoxClicked(int column, GenForm.lyrEDLayer.tabPageAttendanceDetConfigContainer.lyrAttendanceDetailsLayer.tabPageChartRequestedContainer.grdChartRequestRow row, boolean isChecked) throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onGrdInvoicesBillGridCheckBoxClicked(int column, GenForm.lyrEDLayer.tabPageAttendanceDetConfigContainer.lyrAttendanceDetailsLayer.tabPageInvoicesBillingContainer.grdInvoicesBillRow row, boolean isChecked) throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onChkInvoiceBillValueChanged() throws ims.framework.exceptions.PresentationLogicException;
	abstract protected void onChkChartRequestValueChanged() throws ims.framework.exceptions.PresentationLogicException;

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
				onFormOpen(args);
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().setTabActivatedEvent(new ims.framework.delegates.TabActivated()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onlyrEDtabPageAttendanceDetConfigActivated();
			}
		});
		this.form.lyrED().tabPageOther().setTabActivatedEvent(new ims.framework.delegates.TabActivated()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onlyrEDtabPageOtherActivated();
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().btnCancel().setClickEvent(new Click()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onBtnCancelClick();
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().btnSave().setClickEvent(new Click()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onBtnSaveClick();
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().btnEdit().setClickEvent(new Click()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onBtnEditClick();
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().tabPageAttendanceDetalis().setTabActivatedEvent(new ims.framework.delegates.TabActivated()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onlyrAttendanceDetailstabPageAttendanceDetalisActivated();
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().tabPageChartRequested().setTabActivatedEvent(new ims.framework.delegates.TabActivated()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onlyrAttendanceDetailstabPageChartRequestedActivated();
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().tabPageInvoicesBilling().setTabActivatedEvent(new ims.framework.delegates.TabActivated()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onlyrAttendanceDetailstabPageInvoicesBillingActivated();
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().tabPageAttendanceDetalis().grdAttendanceDetailsControlConfig().setGridCheckBoxClickedEvent(new GridCheckBoxClicked()
		{
			private static final long serialVersionUID = 1L;
			public void handle(int column, ims.framework.controls.GridRow row, boolean isChecked) throws ims.framework.exceptions.PresentationLogicException
			{
				onGrdAttendanceDetailsControlConfigGridCheckBoxClicked(column, new GenForm.lyrEDLayer.tabPageAttendanceDetConfigContainer.lyrAttendanceDetailsLayer.tabPageAttendanceDetalisContainer.grdAttendanceDetailsControlConfigRow(row), isChecked);
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().tabPageChartRequested().grdChartRequest().setGridCheckBoxClickedEvent(new GridCheckBoxClicked()
		{
			private static final long serialVersionUID = 1L;
			public void handle(int column, ims.framework.controls.GridRow row, boolean isChecked) throws ims.framework.exceptions.PresentationLogicException
			{
				onGrdChartRequestGridCheckBoxClicked(column, new GenForm.lyrEDLayer.tabPageAttendanceDetConfigContainer.lyrAttendanceDetailsLayer.tabPageChartRequestedContainer.grdChartRequestRow(row), isChecked);
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().tabPageInvoicesBilling().grdInvoicesBill().setGridCheckBoxClickedEvent(new GridCheckBoxClicked()
		{
			private static final long serialVersionUID = 1L;
			public void handle(int column, ims.framework.controls.GridRow row, boolean isChecked) throws ims.framework.exceptions.PresentationLogicException
			{
				onGrdInvoicesBillGridCheckBoxClicked(column, new GenForm.lyrEDLayer.tabPageAttendanceDetConfigContainer.lyrAttendanceDetailsLayer.tabPageInvoicesBillingContainer.grdInvoicesBillRow(row), isChecked);
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().chkInvoiceBill().setValueChangedEvent(new ValueChanged()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onChkInvoiceBillValueChanged();
			}
		});
		this.form.lyrED().tabPageAttendanceDetConfig().chkChartRequest().setValueChangedEvent(new ValueChanged()
		{
			private static final long serialVersionUID = 1L;
			public void handle() throws ims.framework.exceptions.PresentationLogicException
			{
				onChkChartRequestValueChanged();
			}
		});
	}
	private void onlyrEDtabPageAttendanceDetConfigActivated()
	{
		this.form.lyrED().showtabPageAttendanceDetConfig();
	}
	private void onlyrEDtabPageOtherActivated()
	{
		this.form.lyrED().showtabPageOther();
	}
	private void onlyrAttendanceDetailstabPageAttendanceDetalisActivated()
	{
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().showtabPageAttendanceDetalis();
	}
	private void onlyrAttendanceDetailstabPageChartRequestedActivated()
	{
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().showtabPageChartRequested();
	}
	private void onlyrAttendanceDetailstabPageInvoicesBillingActivated()
	{
		this.form.lyrED().tabPageAttendanceDetConfig().lyrAttendanceDetails().showtabPageInvoicesBilling();
	}

	public void free()
	{
		this.engine = null;
		this.form = null;
	}
	public abstract void clearContextInformation();
	protected ims.framework.UIEngine engine;
	protected GenForm form;
}