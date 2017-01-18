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
// This code was generated by Bogdan Tofei using IMS Development Environment (version 1.80 build 5465.13953)
// Copyright (C) 1995-2015 IMS MAXIMS. All rights reserved.

package ims.RefMan.forms.pendingemergencytheatreforward;

import ims.configuration.gen.ConfigFlag;
import ims.core.resource.place.vo.LocationRefVo;
import ims.core.vo.lookups.PatIdType;
import ims.framework.FormName;
import ims.framework.controls.DynamicGridCell;
import ims.framework.controls.DynamicGridColumn;
import ims.framework.controls.DynamicGridRow;
import ims.framework.enumerations.Alignment;
import ims.framework.enumerations.DialogResult;
import ims.framework.enumerations.DynamicCellType;
import ims.framework.enumerations.SortMode;
import ims.framework.exceptions.PresentationLogicException;
import ims.scheduling.vo.PendingEmergencyTheatreListVo;
import ims.scheduling.vo.PendingEmergencyTheatreListVoCollection;

public class Logic extends BaseLogic
{
	private static final long serialVersionUID = 1L;

	private static final String	COLUMN_PATIENT_NAME		= "0";
	private static final String	COLUMN_IDENTIFIER		= "1";
	private static final String	COLUMN_PROCEDURE        = "2";
	private static final String	COLUMN_RESPONSIBLE_HCP 	= "3";
	private static final String	COLUMN_CURRENT_WARD		= "4";
	private static final String	COLUMN_URGENCY   		= "5";
	private static final String COLUMN_EXPECTED_WARD	= "6";
	private static final String	COLUMN_EXPECTED_DATE    = "7";
	
	@Override
	protected void onFormOpen(Object[] args) throws PresentationLogicException
	{
		
		if( args != null && args.length > 0 &&  args[0] instanceof LocationRefVo)
		{
			form.getLocalContext().setWard((LocationRefVo) args[0]);
		}
		
		initialise();
		open();		
	}
	
	private void initialise()
	{
		createDynamicGridResults();
		boolean isDialogScreen = engine.isDialog();
		if (isDialogScreen)
		{
			engine.clearPatientContextInformation();
		}		
	}

	private void createDynamicGridResults()
	{
		
		form.dyngrdResults().setSelectable(true);
		
		// Get default patient identifier
		PatIdType dispIdType = PatIdType.getNegativeInstance(ConfigFlag.UI.DISPLAY_PATID_TYPE.getValue());
		
		DynamicGridColumn column = form.dyngrdResults().getColumns().newColumn("Patient Name", COLUMN_PATIENT_NAME);
		
		column.setWidth(130);
		column.setAlignment(Alignment.LEFT);
		column.setReadOnly(true);
		column.setCanGrow(true);
		column.setSortMode(SortMode.AUTOMATIC);
		
		column = form.dyngrdResults().getColumns().newColumn(dispIdType.getText(), COLUMN_IDENTIFIER);
		
		column.setWidth(80);
		column.setAlignment(Alignment.LEFT);
		column.setReadOnly(true);
		column.setCanGrow(true);
		column.setSortMode(SortMode.AUTOMATIC);
		
		column = form.dyngrdResults().getColumns().newColumn("Procedure", COLUMN_PROCEDURE);
		
		column.setWidth(125);
		column.setAlignment(Alignment.LEFT);
		column.setReadOnly(true);
		column.setCanGrow(true);
		column.setSortMode(SortMode.AUTOMATIC);
		
		column = form.dyngrdResults().getColumns().newColumn("Responsible HCP", COLUMN_RESPONSIBLE_HCP);
		
		column.setWidth(120);
		column.setAlignment(Alignment.LEFT);
		column.setReadOnly(true);
		column.setCanGrow(true);
		column.setSortMode(SortMode.AUTOMATIC);
		
		column = form.dyngrdResults().getColumns().newColumn("Curr. Ward", COLUMN_CURRENT_WARD);
		
		column.setWidth(110);
		column.setAlignment(Alignment.LEFT);
		column.setReadOnly(true);
		column.setCanGrow(true);
		column.setSortMode(SortMode.AUTOMATIC);
		
		column = form.dyngrdResults().getColumns().newColumn("Urgency", COLUMN_URGENCY);
		
		column.setWidth(70);
		column.setAlignment(Alignment.LEFT);
		column.setReadOnly(true);
		column.setCanGrow(true);
		column.setSortMode(SortMode.AUTOMATIC);
		
		column = form.dyngrdResults().getColumns().newColumn("Exp. Ward", COLUMN_EXPECTED_WARD);
		
		column.setWidth(110);
		column.setAlignment(Alignment.LEFT);
		column.setReadOnly(true);
		column.setCanGrow(true);
		column.setSortMode(SortMode.AUTOMATIC);
		
		column = form.dyngrdResults().getColumns().newColumn("Exp. Date", COLUMN_EXPECTED_DATE);
		
		column.setWidth(-1);
		column.setAlignment(Alignment.LEFT);
		column.setReadOnly(true);
		column.setCanGrow(true);
		column.setSortMode(SortMode.AUTOMATIC);
		
	}

	private void open()
	{
		populateResultsGrid(domain.listPendingEmergencyTheatreRecords(form.getLocalContext().getWard()));
		updateControlsState();
	}

	private void populateResultsGrid(PendingEmergencyTheatreListVoCollection records)
	{
		form.dyngrdResults().getRows().clear();
		form.getGlobalContext().Core.setPatientShort(null);
		form.getGlobalContext().Scheduling.setPendingEmergencyTheatre(null);
		form.getGlobalContext().RefMan.setCatsReferral(null);
		
		if (records == null || records.size() == 0)
		{
			engine.showMessage("No records found.");
			return;
		}
		
		for (int i = 0; i < records.size(); i++)
		{
			PendingEmergencyTheatreListVo pendingEmergencyTheatre = records.get(i);
			
			DynamicGridRow newRow = form.dyngrdResults().getRows().newRow();
			
			
			DynamicGridCell cellPatient = newRow.getCells().newCell(getColumnById(COLUMN_PATIENT_NAME), DynamicCellType.STRING);
			
			cellPatient.setReadOnly(true);
			cellPatient.setValue( pendingEmergencyTheatre.getPatient() != null && pendingEmergencyTheatre.getPatient().getName() != null ? pendingEmergencyTheatre.getPatient().getName().toStringSurnameFirst() : null);
			cellPatient.setTooltip(pendingEmergencyTheatre.getPatient() != null && pendingEmergencyTheatre.getPatient().getName() != null ? pendingEmergencyTheatre.getPatient().getName().toStringSurnameFirst() : null);	
			
			
			DynamicGridCell cellPatID = newRow.getCells().newCell(getColumnById(COLUMN_IDENTIFIER), DynamicCellType.STRING);
			
			cellPatID.setReadOnly(true);
			cellPatID.setValue( pendingEmergencyTheatre.getPatient() != null && pendingEmergencyTheatre.getPatient().getDisplayId() != null ? pendingEmergencyTheatre.getPatient().getDisplayId().getIdValue() : null);
			cellPatID.setTooltip(pendingEmergencyTheatre.getPatient() != null && pendingEmergencyTheatre.getPatient().getDisplayId() != null ? pendingEmergencyTheatre.getPatient().getDisplayId().getIdValue() : null);
			
			
			DynamicGridCell cellProcedure = newRow.getCells().newCell(getColumnById(COLUMN_PROCEDURE), DynamicCellType.STRING);
			
			cellProcedure.setReadOnly(true);
			
			String procedure = "";
			
			if (pendingEmergencyTheatre.getPrimaryProcedure() != null)
			{
				procedure += pendingEmergencyTheatre.getPrimaryProcedure().getProcedureName();
				
				if (pendingEmergencyTheatre.getSecondaryProcedure() != null)
				{
					procedure += ", " + pendingEmergencyTheatre.getSecondaryProcedure().getProcedureName();
				}
			}
			
			cellProcedure.setValue(procedure);
			cellProcedure.setTooltip(procedure);
			
			DynamicGridCell cellResponsibleHCP = newRow.getCells().newCell(getColumnById(COLUMN_RESPONSIBLE_HCP), DynamicCellType.STRING);
			
			cellResponsibleHCP.setReadOnly(true);
			cellResponsibleHCP.setValue( pendingEmergencyTheatre.getResponsibleHCP() != null &&  pendingEmergencyTheatre.getResponsibleHCP().getName() != null ? pendingEmergencyTheatre.getResponsibleHCP().getName().toString() : null);
			cellResponsibleHCP.setTooltip(pendingEmergencyTheatre.getResponsibleHCP() != null &&  pendingEmergencyTheatre.getResponsibleHCP().getName() != null ? pendingEmergencyTheatre.getResponsibleHCP().getName().toString() : null);
			
			DynamicGridCell cellUrgency = newRow.getCells().newCell(getColumnById(COLUMN_URGENCY), DynamicCellType.STRING);
			
			cellUrgency.setReadOnly(true);
			cellUrgency.setValue( pendingEmergencyTheatre.getUrgencyCategory() != null ? pendingEmergencyTheatre.getUrgencyCategory().getText() : null);
			cellUrgency.setTooltip(pendingEmergencyTheatre.getUrgencyCategory() != null ? pendingEmergencyTheatre.getUrgencyCategory().getText() : null);
			
			DynamicGridCell cellCurrentWard = newRow.getCells().newCell(getColumnById(COLUMN_CURRENT_WARD), DynamicCellType.STRING);
			
			cellCurrentWard.setReadOnly(true);
			cellCurrentWard.setValue( pendingEmergencyTheatre.getPatient() != null && pendingEmergencyTheatre.getPatient().getWard() != null ? pendingEmergencyTheatre.getPatient().getWard().getName() : null);
			cellCurrentWard.setTooltip(pendingEmergencyTheatre.getPatient() != null && pendingEmergencyTheatre.getPatient().getWard() != null ? pendingEmergencyTheatre.getPatient().getWard().getName() : null);
			
			DynamicGridCell cellExpectedWard = newRow.getCells().newCell(getColumnById(COLUMN_EXPECTED_WARD), DynamicCellType.STRING);
			
			cellExpectedWard.setReadOnly(true);
			cellExpectedWard.setValue( pendingEmergencyTheatre.getExpectedWard() != null ? pendingEmergencyTheatre.getExpectedWard().getName() : null);
			cellExpectedWard.setTooltip( pendingEmergencyTheatre.getExpectedWard() != null ? pendingEmergencyTheatre.getExpectedWard().getName() : null);
			
			DynamicGridCell cellExpectedDate = newRow.getCells().newCell(getColumnById(COLUMN_EXPECTED_DATE), DynamicCellType.DATE);
			
			cellExpectedDate.setReadOnly(true);
			cellExpectedDate.setValue( pendingEmergencyTheatre.getExpectedDateTime() != null ? pendingEmergencyTheatre.getExpectedDateTime().getDate() : null);
			
			newRow.setValue(pendingEmergencyTheatre);
		
		}
		
	}

	private DynamicGridColumn getColumnById(String identifier)
	{
		return form.dyngrdResults().getColumns().getByIdentifier(identifier);
	}	
		
	@Override
	protected void onBtnCloseClick() throws ims.framework.exceptions.PresentationLogicException
	{
		engine.close(DialogResult.OK);
	}

	@Override
	protected void onDyngrdResultsRowSelectionChanged(ims.framework.controls.DynamicGridRow row)
	{
		if (form.dyngrdResults().getValue() != null)
		{
			form.getGlobalContext().Core.setPatientShort(domain.getPatientShort(((PendingEmergencyTheatreListVo)form.dyngrdResults().getValue()).getPatient()));
		}
		
		updateControlsState();
	}

	private void updateControlsState()
	{
		form.getContextMenus().RefMan.hideAllPendingEmergencyTheatreMenuItems();
		
		form.getContextMenus().RefMan.getPendingEmergencyTheatreVIEW_EDIT_EMERGENCY_THEATREItem().setVisible(form.dyngrdResults().getSelectedRow() != null);
		
	}

	@Override
	protected void onContextMenuItemClick(int menuItemID, ims.framework.Control sender) throws ims.framework.exceptions.PresentationLogicException
	{
		switch (menuItemID)
		{
			case GenForm.ContextMenus.RefManNamespace.PendingEmergencyTheatre.VIEW_EDIT_EMERGENCY_THEATRE:
				
				form.getGlobalContext().Core.setPatientShort(domain.getPatientShort(((PendingEmergencyTheatreListVo)form.dyngrdResults().getValue()).getPatient()));
				form.getGlobalContext().Scheduling.setPendingEmergencyTheatre((PendingEmergencyTheatreListVo)form.dyngrdResults().getValue());
				form.getGlobalContext().RefMan.setCatsReferral(((PendingEmergencyTheatreListVo)form.dyngrdResults().getValue()).getCatsReferral());
				
				engine.open(form.getForms().RefMan.AddEmergencyTheatre, new Object[] {Boolean.TRUE});
				
			break;
		}
	}

	@Override
	protected void onFormDialogClosed(FormName formName, DialogResult result) throws PresentationLogicException
	{
		if (formName.equals(form.getForms().RefMan.AddEmergencyTheatre))
		{
			open();
		}
		
	}
	
}
