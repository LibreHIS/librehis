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
// This code was generated by Cornel Ventuneac using IMS Development Environment (version 1.80 build 4535.14223)
// Copyright (C) 1995-2012 IMS MAXIMS. All rights reserved.

package ims.clinical.forms.surgicalauditpreoperationcheckstheatrenurse;


import ims.clinical.forms.surgicalauditpreoperationcheckstheatrenurse.GenForm.grdPlannedProceduresRow;
import ims.clinical.vo.PatientProcedureForSurgicalAuditPreOpChecksVo;
import ims.clinical.vo.PatientProcedureForSurgicalAuditPreOpChecksVoCollection;
import ims.clinical.vo.SurgicalAuditOperationDetailRefVo;
import ims.clinical.vo.SurgicalAuditOperationDetailStatusVo;
import ims.clinical.vo.SurgicalAuditOperationDetailVo;
import ims.clinical.vo.SurgicalAuditPreOpChecksTheatreNurseVo;
import ims.clinical.vo.SurgicalAuditPreOpChecksVo;
import ims.clinical.vo.enums.SurgicalAuditPreOpChecksEvent;
import ims.clinical.vo.lookups.SurgicalAuditOperationDetailStatus;
import ims.configuration.gen.ConfigFlag;
import ims.core.admin.vo.CareContextRefVo;
import ims.core.patient.vo.PatientRefVo;
import ims.core.vo.AuthoringInformationVo;
import ims.core.vo.HcpLiteVo;
import ims.core.vo.LocSiteLiteVo;
import ims.core.vo.LocationLiteVo;
import ims.core.vo.LocationLiteVoCollection;
import ims.core.vo.MemberOfStaffLiteVo;
import ims.core.vo.NurseVo;
import ims.core.vo.enums.MosType;
import ims.core.vo.lookups.HcpDisType;
import ims.core.vo.lookups.LocationType;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.exceptions.UniqueKeyViolationException;
import ims.framework.FormName;
import ims.framework.enumerations.DialogResult;
import ims.framework.enumerations.FormMode;
import ims.framework.enumerations.SortOrder;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.DateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Logic extends BaseLogic
{
	private static final long serialVersionUID = 1L;

	
	protected void onBtnCancelClick() throws ims.framework.exceptions.PresentationLogicException
	{
		//open();
		form.getLocalContext().setselectedEvent(SurgicalAuditPreOpChecksEvent.CANCEL);
		form.fireCustomControlValueChanged();
	}
	
	protected void onBtnSaveClick() throws ims.framework.exceptions.PresentationLogicException
	{
		if(	save())
		{
			//open();
			form.getLocalContext().setselectedEvent(SurgicalAuditPreOpChecksEvent.SAVE);
			form.fireCustomControlValueChanged();
		}

	}
	
	private boolean save()
	{
		String[] errors = validateUIRules();
		if (errors != null && errors.length != 0)
		{
			engine.showErrors(errors);
			return false;
		}
		SurgicalAuditOperationDetailVo record = form.getLocalContext().getOperationDetail();
		record = populateDataFromScreen(record);
		errors = record.validate();
		if (errors != null && errors.length != 0)
		{
			engine.showErrors(errors);
			return false;
		}

		try 
		{
			form.getLocalContext().setOperationDetail(domain.saveSurgicalAuditOperationDetailsVo(record));
			/*
			if (record.getPreOpChecksTheatreNurse().getID_SurgicalAuditPreOpChecksTheatreNurse()==null)//new
				form.getLocalContext().setOperationDetail(domain.saveSurgicalAuditOperationDetailsVo(record));
			else
				domain.savePreOpChecksTheatreNurse(record.getPreOpChecksTheatreNurse());
			*/
		}
		catch (StaleObjectException e) 
		{
			engine.showMessage(ConfigFlag.UI.STALE_OBJECT_MESSAGE.getValue());
			return true;
		}
		catch (UniqueKeyViolationException e) 
		{
			engine.showMessage(e.getMessage());
			return false;
		}
		//form.getLocalContext().setselectedEvent(SurgicalAuditPreOpChecksEvent.SAVE);
		//form.fireCustomControlValueChanged();
		return true;
		
	}
	private String[] validateUIRules()
	{
		List<String> uiErrors = new ArrayList<String>();

		if (form.grdPlannedProcedures().getRows().size() == 0)
		{
			uiErrors.add("Planned Procedures are mandatory.");
		}
		/*if (form.dtimTheatreNurse().getValue() != null && form.dtimTheatreNurse().getValue().isGreaterThan(new DateTime())) //wdev-15735
		{
			uiErrors.add("Date/ Time cannot be set in the future!");
		}*/
		/*if( form.dtimEnterOperatingTheatre().getValue() == null)
		{
			uiErrors.add("Enter Operating Theatre is mandatory.");
		}

		if (form.dtimEnterOperatingTheatre().getValue() != null && form.dtimEnterOperatingTheatre().getValue().isGreaterThan(new DateTime()))
		{
			uiErrors.add("Enter Operating Theatre cannot be set in the future!");
		}*/
		//wdev-15749
		if( !Boolean.TRUE.equals(form.chkLocumAccompanyingTheatreNurse().getValue()) && form.ccTheatreNurse().getValue() == null)
		{
			uiErrors.add("Accompanying Theatre Nurse is mandatory.");
		}
		else if( Boolean.TRUE.equals(form.chkLocumAccompanyingTheatreNurse().getValue()) && form.txtAccompanyingTheatreNurse().getValue() == null)
		{
			uiErrors.add("Accompanying Theatre Nurse is mandatory.");
		}
		/*
		if( form.dtimTheatreNurse().getValue() == null)
		{
			uiErrors.add("Theatre Nurse Date/Time is mandatory.");
		}*/
		
		/*if (!(form.GroupConfirmPatDet().getValue().equals(GroupConfirmPatDetEnumeration.rdoConfirmPatientDetailsYes) || form.GroupConfirmPatDet().getValue().equals(GroupConfirmPatDetEnumeration.rdoConfirmPatientDetailsNo)))
		{
			uiErrors.add("Confirm Patient Details is mandatory.");
		}*/

		if (form.cmbTheatreNameNumber().getValue() == null)
		{
			uiErrors.add("Theatre Name/ Number is mandatory.");
		}
		
		/*if (!(form.GroupPreOpCheckCompleted().getValue().equals(GroupPreOpCheckCompletedEnumeration.rdoPreOpCheckCompletedYes) || form.GroupPreOpCheckCompleted().getValue().equals(GroupPreOpCheckCompletedEnumeration.rdoPreOpCheckCompletedNo)))
		{
			uiErrors.add(" Pre Op Check Completed is mandatory.");
		}
		
		if (form.GroupPreOpCheckCompleted().getValue() != null  && form.GroupPreOpCheckCompleted().getValue().equals(GroupPreOpCheckCompletedEnumeration.rdoPreOpCheckCompletedNo))
		{
			if (form.txtComments().getValue() == null)
			{
				uiErrors.add("Comments are mandatory.");
			}
			
		}*/
		if (!isPrimaryProcedureInGrid())
		{
			uiErrors.add("One procedure should be marked as primary for each Surgical Audit Operation Detail.");
		}
		
		String[] result = new String[uiErrors.size()];
		uiErrors.toArray(result);
		return result;
	}
	
	private SurgicalAuditOperationDetailVo populateDataFromScreen(SurgicalAuditOperationDetailVo record)
	{
		if( record == null)
			return null;
				
		PatientProcedureForSurgicalAuditPreOpChecksVoCollection tempVoColl = new PatientProcedureForSurgicalAuditPreOpChecksVoCollection();
		for( int i = 0; i < form.grdPlannedProcedures().getRows().size();i++)
		{
			PatientProcedureForSurgicalAuditPreOpChecksVo tempVo = form.grdPlannedProcedures().getRows().get(i).getValue();
			tempVo.setIsPrimary(form.grdPlannedProcedures().getRows().get(i).getColumnPrimary());
			tempVoColl.add(tempVo);
		}
		if( record.getPreOpChecksIsNotNull())
		{
			if( tempVoColl != null && tempVoColl.size() > 0)
				record.getPreOpChecks().setPlannedProcedures(tempVoColl);
		}
		SurgicalAuditPreOpChecksTheatreNurseVo tempVo = record.getPreOpChecksTheatreNurse(); 
		if( tempVo == null)
			tempVo = new SurgicalAuditPreOpChecksTheatreNurseVo();
		
		if( tempVo.getPatient() == null)
			tempVo.setPatient(form.getLocalContext().getPatientRef());
		
		if( tempVo.getCareContext() == null)
			tempVo.setCareContext(form.getLocalContext().getCareContext());
		
		//tempVo.setEnterOpTheatreTime(form.dtimEnterOperatingTheatre().getValue());
		
		if( tempVo.getAuthoringInformation() == null)
		{
			AuthoringInformationVo tempAuthInfVo = new AuthoringInformationVo();
			if( domain.getHcpLiteUser() instanceof HcpLiteVo)
				tempAuthInfVo.setAuthoringHcp((HcpLiteVo) domain.getHcpLiteUser());
			
			tempAuthInfVo.setAuthoringDateTime(new DateTime());
			tempVo.setAuthoringInformation(tempAuthInfVo);
						
		}
		//wdev-15717
		/*AuthoringInformationVo tempAuthInfVo1 = new AuthoringInformationVo();
		if( form.ccTheatreNurse().getValue() instanceof NurseVo)
		{
			Hcp  tempHcp = (Hcp) form.ccTheatreNurse().getValue();
			HcpLiteVo hcpLitevo = domain.getHcpLiteVo(tempHcp);
			tempAuthInfVo1.setAuthoringHcp(hcpLitevo);
		}
		else if( form.ccTheatreNurse().getValue() instanceof HcpLiteVo)
		{
			tempAuthInfVo1.setAuthoringHcp((HcpLiteVo) form.ccTheatreNurse().getValue());
		}
		
		//tempAuthInfVo1.setAuthoringHcp((HcpLiteVo) .);
		tempAuthInfVo1.setAuthoringDateTime(form.dtimTheatreNurse().getValue());
		tempVo.setTheatreNurseInformation(tempAuthInfVo1);*/
		tempVo.setAccompanyingTheatreNurse((NurseVo) form.ccTheatreNurse().getValue());  //wdev-15749
		tempVo.setAccompanyingTheatreNurseLocumBool(form.chkLocumAccompanyingTheatreNurse().getValue()); //wdev-15749
		tempVo.setAccompanyingTheatreNurseLocumNurse(form.txtAccompanyingTheatreNurse().getValue());	//wdev-15749
		//---------
		/*if (form.GroupConfirmPatDet().getValue() != null && form.GroupConfirmPatDet().getValue().equals(GroupConfirmPatDetEnumeration.rdoConfirmPatientDetailsYes))
			tempVo.setConfirmPatientDetails(YesNo.YES);	
		else if (form.GroupConfirmPatDet().getValue() != null && form.GroupConfirmPatDet().getValue().equals(GroupConfirmPatDetEnumeration.rdoConfirmPatientDetailsNo))
			tempVo.setConfirmPatientDetails(YesNo.NO);	  
		else
			tempVo.setConfirmPatientDetails(null);*/
		
		tempVo.setTheatreName(form.cmbTheatreNameNumber().getValue());
		tempVo.setOperationType(form.cmbOperationType().getValue()); //wdev-15720
		
		/*if (form.GroupPreOpCheckCompleted().getValue() != null && form.GroupPreOpCheckCompleted().getValue().equals(GroupPreOpCheckCompletedEnumeration.rdoPreOpCheckCompletedYes))
			tempVo.setPreOpCheckCompleted(YesNo.YES);	
		else if (form.GroupPreOpCheckCompleted().getValue() != null && form.GroupPreOpCheckCompleted().getValue().equals(GroupPreOpCheckCompletedEnumeration.rdoPreOpCheckCompletedNo))
			tempVo.setPreOpCheckCompleted(YesNo.NO);	  
		else
			tempVo.setPreOpCheckCompleted(null);*/
					
		tempVo.setComments(form.txtComments().getValue());
		record.setPreOpChecksTheatreNurse(tempVo);
		
		//if( !record.getCurrentStatusIsNotNull() || ( record.getCurrentStatusIsNotNull() && !record.getCurrentStatus().getOperationStatusIsNotNull() ) || ( record.getCurrentStatusIsNotNull() && record.getCurrentStatus().getOperationStatusIsNotNull() && !SurgicalAuditOperationDetailStatus.CANCELLED_OPERATIONS.equals(record.getCurrentStatus().getOperationStatus()) ))
		
		/*if( record.getPreOpChecksTheatreNurseIsNotNull() && record.getPreOpChecksTheatreNurse().getID_SurgicalAuditPreOpChecksTheatreNurse() == null 
			&& record.getCurrentStatus() != null && !SurgicalAuditOperationDetailStatus.CANCELLED_OPERATIONS.equals(record.getCurrentStatus().getOperationStatus())
			&& SurgicalAuditOperationDetailStatus.LEFT_HOLDING_BAY.equals(record.getCurrentStatus().getOperationStatus()))
		{	
			Object mos = domain.getMosUser();

			MemberOfStaffLiteVo statusAuthoringUser = null;
			if (mos instanceof MemberOfStaffLiteVo)
				statusAuthoringUser = ((MemberOfStaffLiteVo) mos);

			SurgicalAuditOperationDetailStatusVo tempCurrentStatus = populateStatus(ims.clinical.vo.lookups.SurgicalAuditOperationDetailStatus.ENTERED_OPERATING_THEATRE, new DateTime(), statusAuthoringUser);
			record.setCurrentStatus(tempCurrentStatus);

			// Populate Status History
			
			SurgicalAuditOperationDetailStatusVoCollection tempStatusHistory = record.getStatusHistory();
			tempStatusHistory.add(tempCurrentStatus);
			record.setStatusHistory(tempStatusHistory);
	
		}*/
		return record;
		
	}
	private SurgicalAuditOperationDetailStatusVo populateStatus(SurgicalAuditOperationDetailStatus status, DateTime dateTime, MemberOfStaffLiteVo statusAuthoringUser)
	{
		SurgicalAuditOperationDetailStatusVo statusRecord = new SurgicalAuditOperationDetailStatusVo();

		statusRecord.setOperationStatus(status);
		statusRecord.setStatusDateTime(dateTime);
		statusRecord.setAuthoringUser(statusAuthoringUser);

		return statusRecord;
	}
	
	protected void onContextMenuItemClick(int menuItemID, ims.framework.Control sender) throws ims.framework.exceptions.PresentationLogicException
	{
		form.getGlobalContext().Clinical.setPatientProcedureMarkedAsPrimary(getPatientProcedureMarkedAsPrimary());  //needed for wdev-15092
		
		switch(menuItemID)
		{
			case GenForm.ContextMenus.ClinicalNamespace.SurgicalAuditPlannedProcedures.EDIT :
				form.getGlobalContext().Clinical.setSelectedPatientProcedureFromSurgicalAudit(form.grdPlannedProcedures().getValue());
				engine.open(form.getForms().Clinical.PatientProceduresDialog);		
			break;
			
		}
	}
	private void open()
	{
		clearScreen();
		if( form.getLocalContext().getSurgicalAuditOperationDetailsIsNotNull())
			form.getLocalContext().setOperationDetail(domain.getSurgicalAuditOperationDetailsVo(form.getLocalContext().getSurgicalAuditOperationDetails()));
		else
			form.getLocalContext().setOperationDetail(null);
		
		populateScreenFromData(form.getLocalContext().getOperationDetail());
		form.setMode(FormMode.VIEW);
		updateContextMenu();
	}
	private void populateScreenFromData(SurgicalAuditOperationDetailVo record)
	{
		if(	record == null)
			return;
		
		populatePlannedProceduresGrid(record.getPreOpChecks());
		if(	record.getPreOpChecksTheatreNurseIsNotNull())
		{
			
			
			ArrayList<LocationLiteVo> theatre = form.cmbTheatreNameNumber().getValues();
			if( theatre != null && record.getPreOpChecksTheatreNurse().getTheatreNameIsNotNull() && !theatre.contains(record.getPreOpChecksTheatreNurse().getTheatreName()))
				form.cmbTheatreNameNumber().newRow(record.getPreOpChecksTheatreNurse().getTheatreName(),record.getPreOpChecksTheatreNurse().getTheatreName().getName());
			
			form.cmbTheatreNameNumber().setValue(record.getPreOpChecksTheatreNurse().getTheatreName());
			form.txtComments().setValue(record.getPreOpChecksTheatreNurse().getComments());
			
			form.ccTheatreNurse().setValue(record.getPreOpChecksTheatreNurse().getAccompanyingTheatreNurse()); //wdev-15749
			form.chkLocumAccompanyingTheatreNurse().setValue(record.getPreOpChecksTheatreNurse().getAccompanyingTheatreNurseLocumBool()); //wdev-15749
			form.txtAccompanyingTheatreNurse().setValue(record.getPreOpChecksTheatreNurse().getAccompanyingTheatreNurseLocumNurse()); //wdev-15479
			
			form.cmbOperationType().setValue(record.getPreOpChecksTheatreNurse().getOperationType()); //wdev-15720
		}
		
	}
	private void populatePlannedProceduresGrid(SurgicalAuditPreOpChecksVo record)
	{
		PatientProcedureForSurgicalAuditPreOpChecksVoCollection collProcedure = domain.listProcedures(record);

		if (collProcedure == null)
			return;
		
		if( collProcedure.size() > 0)
		{
			for(int i = 0; i < collProcedure.size();i++)
			{
				PatientProcedureForSurgicalAuditPreOpChecksVo tempVo = collProcedure.get(i);
			
				if( tempVo != null)
				{
					grdPlannedProceduresRow row = form.grdPlannedProcedures().getRows().newRow();
					row.setColumnProcedures(tempVo.getProcedureDescription());
					row.setTooltipForColumnProcedures(tempVo.getProcedureDescription());
					row.setColumnPrimary(tempVo.getIsPrimaryIsNotNull() ? (tempVo.getIsPrimary().equals(Boolean.TRUE)? Boolean.TRUE:Boolean.FALSE):Boolean.FALSE);
					row.setValue(tempVo);
					
				}
			}
		}
	}
	private void clearScreen()
	{
		clearPlannedProcedures();
		
		
		form.cmbTheatreNameNumber().setValue(null);
		
		form.txtComments().setValue(null);
		
		form.ccTheatreNurse().clear();
		form.ccTheatreNurse().setValue(null);
		form.cmbOperationType().setValue(null);
		form.txtAccompanyingTheatreNurse().setValue(null); //wdev-15749
		form.chkLocumAccompanyingTheatreNurse().setValue(null); //wdev-15749
	}
	private void clearPlannedProcedures()
	{
		form.grdPlannedProcedures().getRows().clear();
	}

	protected void onBtnEditClick() throws PresentationLogicException 
	{
		HcpLiteVo tempHcp = null;
		if( domain.getHcpLiteUser() instanceof HcpLiteVo)
			tempHcp = (HcpLiteVo) domain.getHcpLiteUser();
		if( tempHcp == null)
		{
			engine.showMessage("The current user is not an HCP");
			return;
		}
		else 
		{
			
		}
		// Check SOE on EDIT
		if (form.getLocalContext().getOperationDetail()!=null && form.getLocalContext().getOperationDetail().getID_SurgicalAuditOperationDetailIsNotNull() && domain.isStale(form.getLocalContext().getOperationDetail()))
		{
			form.getLocalContext().setOperationDetail(domain.getSurgicalAuditOperationDetailsVo(form.getLocalContext().getOperationDetail()));
			clearScreen();
			populateScreenFromData(form.getLocalContext().getOperationDetail());
		}
		//wdev-15717
		/*if(form.ccTheatreNurse().getValue() == null && form.getLocalContext().getOperationDetail()!=null && (form.getLocalContext().getOperationDetail().getPreOpChecksTheatreNurse() == null  || form.getLocalContext().getOperationDetail().getPreOpChecksTheatreNurse().getID_SurgicalAuditPreOpChecksTheatreNurse() == null))
		{
			HcpDisType hcptype =getParentNodeHcp(tempHcp.getHcpType()); 
			if( hcptype != null && hcptype.equals(HcpDisType.NURSING))
			{
				form.ccTheatreNurse().setValue(tempHcp);
				//form.dtimTheatreNurse().setValue(new DateTime());
			}
		}*/
		form.getLocalContext().setselectedEvent(SurgicalAuditPreOpChecksEvent.EDIT);
		form.fireCustomControlValueChanged();
		
		form.setMode(FormMode.EDIT);
		
	}
	private HcpDisType getParentNodeHcp(HcpDisType hcpvo)
	{
		if( hcpvo == null)
			return null;
		if( hcpvo.getParent() == null)
			return hcpvo;
		else
			return getParentNodeHcp(hcpvo.getParent());
		
	}
	
	
	protected void onFormDialogClosed(FormName formName, DialogResult result)	throws PresentationLogicException 
	{
		if (formName.equals(form.getForms().Clinical.PatientProceduresDialog) && result.equals(DialogResult.OK))
		{
			if (form.getGlobalContext().Clinical.getSelectedPatientProcedureFromSurgicalAudit().equals(form.grdPlannedProcedures().getValue()) )
			{
				updateRowToProcedureGrid(form.getGlobalContext().Clinical.getSelectedPatientProcedureFromSurgicalAudit());
			}
		}
		
	}

	protected void onGrdPlannedProceduresSelectionChanged()	throws PresentationLogicException 
	{
		form.getGlobalContext().Clinical.setSelectedPatientProcedureFromSurgicalAudit(form.grdPlannedProcedures().getValue());
		updateContextMenu();
	}
	private void updateContextMenu()
	{
		form.getContextMenus().Clinical.hideAllSurgicalAuditPlannedProceduresMenuItems();
		form.getContextMenus().Clinical.getSurgicalAuditPlannedProceduresEDITItem().setVisible(form.getMode().equals(FormMode.EDIT) && form.grdPlannedProcedures().getValue() != null );
	}
	
	protected void onFormModeChanged() 
	{
		updateContextMenu();
		updateControlState();
		
	}

	public void initialize(PatientRefVo patientRef,	CareContextRefVo careContextRef,SurgicalAuditOperationDetailRefVo surgicalAuditOperationDetailsRef) 
	{
		form.getLocalContext().setConfirmPatDet(null);
		form.getLocalContext().setCareContext(careContextRef);
		form.getLocalContext().setPatientRef(patientRef);
		form.getLocalContext().setSurgicalAuditOperationDetails(surgicalAuditOperationDetailsRef);
		//wdev-15717
		LocationLiteVo  locVo= null;
		LocSiteLiteVo	locSiteVo = null;
		if( domain.getCurrentLocation() instanceof LocationLiteVo)
		{
			locVo = (LocationLiteVo) domain.getCurrentLocation();
		}
		else if( domain.getCurrentLocation() instanceof LocSiteLiteVo)
		{
			locSiteVo = (LocSiteLiteVo) domain.getCurrentLocation();
		}
		if( locVo != null )
		{
			if(locVo.getTypeIsNotNull() && locVo.getType().equals(LocationType.HOSP))
			{
				form.ccTheatreNurse().initialize(MosType.HCP, HcpDisType.NURSING,locVo.getID_Location());
			}
			else if(locVo.getTypeIsNotNull() && !locVo.getType().equals(LocationType.HOSP))
			{
				locVo = domain.getLocationLite(locVo);
				form.ccTheatreNurse().initialize(MosType.HCP, HcpDisType.NURSING,locVo.getID_Location());
			}
		}
		else if( locSiteVo != null)
		{
			locVo = domain.getLocationLite(locSiteVo);
			form.ccTheatreNurse().initialize(MosType.HCP, HcpDisType.NURSING,locVo.getID_Location());
		}
		//----------------
		
		
		
		populateTheatreCombo();
		
		open();
	
	}
	private void populateTheatreCombo()
	{
		form.cmbTheatreNameNumber().clear();
		LocationLiteVo  locVo= null,tempLocVo = null;
		LocSiteLiteVo	locSiteVo = null;
		if(	domain.getCurrentLocation() instanceof LocationLiteVo)
		{
			locVo = (LocationLiteVo) domain.getCurrentLocation();
		}
		else if( domain.getCurrentLocation() instanceof LocSiteLiteVo)
		{
			locSiteVo = (LocSiteLiteVo) domain.getCurrentLocation();
		}
		if( locVo != null )
		{
			tempLocVo = locVo;
		}
		else if( locSiteVo != null)
		{
			tempLocVo = domain.getLocationLiteVo(locSiteVo);
		}
		
		//WDEV-15675
		LocationLiteVoCollection collLoc = null;
		if (tempLocVo != null)
		{
			if (LocationType.HOSP.equals(tempLocVo.getType()))
				collLoc = domain.listLocationsByParentLocation(tempLocVo,LocationType.THEATRE);
			else
				collLoc = domain.listActiveLocationsAtTheSameLevelWithLocation(tempLocVo,LocationType.THEATRE);

		}
		if (collLoc != null && collLoc.size()>0)
		{
			for (int i = 0; i < collLoc.size(); i++)
			{
				form.cmbTheatreNameNumber().newRow(collLoc.get(i), collLoc.get(i).getName());
			}
		}
	}
	
	public class LocComparator implements Comparator
	{
		private int direction = 1;
		
		public LocComparator()
		{
			this(SortOrder.ASCENDING);
		}
		
		public LocComparator(SortOrder order)
		{
			if (order == SortOrder.DESCENDING)
				direction = -1;
			
		}
		public int compare(Object ob1, Object ob2) 
		{
			String pdate1 = null;
			String pdate2 = null;
			if(ob1 instanceof LocationLiteVo )
			{
				LocationLiteVo ps1 = (LocationLiteVo)ob1;
				pdate1 = ps1.getName();
			}
			if(ob2 instanceof LocationLiteVo)
			{
				LocationLiteVo ps2 = (LocationLiteVo)ob2;
				pdate2 = ps2.getName();
			}
			if(pdate1 != null )
				return  pdate1.compareTo(pdate2)*direction;
			if(pdate2 != null)
				return (-1)*direction;
			
			return 0;
		}
	
	}
	
	protected void onGrdPlannedProceduresGridCheckBoxClicked(int column, grdPlannedProceduresRow row, boolean isChecked) throws PresentationLogicException 
	{
		PatientProcedureForSurgicalAuditPreOpChecksVo tempvo1 = row.getValue();
		tempvo1.setIsPrimary(row.getColumnPrimary());
		for(int i = 0;i < form.grdPlannedProcedures().getRows().size();i++)
		{
			grdPlannedProceduresRow row1 = form.grdPlannedProcedures().getRows().get(i); 
			if(!row1.getValue().equals(row.getValue()) && row1.getColumnPrimary() == true)
			{
				row1.setColumnPrimary(false);
				PatientProcedureForSurgicalAuditPreOpChecksVo tempvo = row1.getValue();
				tempvo.setIsPrimary(false);
				row1.setValue(tempvo);
								
			}
		}
		
	}
	private void updateRowToProcedureGrid(PatientProcedureForSurgicalAuditPreOpChecksVo patientProcedure)
	{
		grdPlannedProceduresRow row = form.grdPlannedProcedures().getSelectedRow();
		row.setColumnProcedures(patientProcedure.getProcedureDescription());
		row.setTooltipForColumnProcedures(patientProcedure.getProcedureDescription());
		row.setColumnPrimary(patientProcedure.getIsPrimary());
		row.setValue(patientProcedure);
	}
	private PatientProcedureForSurgicalAuditPreOpChecksVo getPatientProcedureMarkedAsPrimary()
	{
		for (int i=0;i<form.grdPlannedProcedures().getRows().size();i++)
		{
			if (form.grdPlannedProcedures().getRows().get(i).getColumnPrimary()== true)
			{
				return form.grdPlannedProcedures().getRows().get(i).getValue();
			}
		}
		return null;
	}
	private Boolean isPrimaryProcedureInGrid()
	{
		for (int i=0;i<form.grdPlannedProcedures().getRows().size();i++)
		{
			if (form.grdPlannedProcedures().getRows().get(i).getColumnPrimary()== true)
			{
				return true;
			}
		}
		return false;
	}

	
	public SurgicalAuditPreOpChecksEvent getSelectedEvent() 
	{
		return form.getLocalContext().getselectedEvent();
	}

	
	public void resetSelectedEvent() 
	{
		form.getLocalContext().setselectedEvent(null);
		
	}

	
	


	public Boolean getConfirmPatDet() 
	{
		
		/*if( form.GroupConfirmPatDet().getValue().equals(GroupConfirmPatDetEnumeration.rdoConfirmPatientDetailsNo))
		{
			return false;
		}
		else 
		{
			return true;
		}
		*/
		return false;
	}

	
	public void setConfirmPatDet(Boolean enabl) 
	{
		form.getLocalContext().setConfirmPatDet(enabl);
	}

	
	
	private void updateControlState()
	{
		if(  form.getMode().equals(FormMode.EDIT))
		{
			form.chkLocumAccompanyingTheatreNurse().setEnabled(true);
			form.txtComments().setEnabled(true);
			LocumChanged();
			
			
		}
		else
		{
			form.ccTheatreNurse().setEnabled(false);			//wdev-15717
			form.ccTheatreNurse().isRequired(false);		//wdev-15717
		}
	}

	
	protected void onFormOpen(Object[] args) throws PresentationLogicException 
	{
		// TODO Auto-generated method stub
		
	}

	
	protected void onCcTheatreNurseValueChanged() throws PresentationLogicException 
	{
		
		
	}

	//wdev-15749
	protected void onChkLocumAccompanyingTheatreNurseValueChanged()	throws PresentationLogicException 
	{
		LocumChanged();
		
	}
	//wdev-15749
	private void LocumChanged()
	{
		if( Boolean.TRUE.equals(form.chkLocumAccompanyingTheatreNurse().getValue()))
		{
			form.ccTheatreNurse().setEnabled(false);
			form.ccTheatreNurse().isRequired(false);
			form.ccTheatreNurse().clear();
			form.txtAccompanyingTheatreNurse().setEnabled(true);
		}
		else
		{
			form.ccTheatreNurse().setEnabled(true);
			form.ccTheatreNurse().isRequired(true);
			form.txtAccompanyingTheatreNurse().setEnabled(false);
			form.txtAccompanyingTheatreNurse().setValue(null);
			
		}
		
	}

}
