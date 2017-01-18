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
// This code was generated by Silviu Checherita using IMS Development Environment (version 1.80 build 5256.13787)
// Copyright (C) 1995-2014 IMS MAXIMS. All rights reserved.

package ims.RefMan.forms.patientcodinglist;

import ims.RefMan.vo.AdmissionDetailForPatientCodingListVo;
import ims.RefMan.vo.AdmissionDetailsWithConsultantAndWardStayVo;
import ims.RefMan.vo.BookingAppointmentForPatientCodingListVo;
import ims.RefMan.vo.ConsultantStayForPatientCodingListVo;
import ims.RefMan.vo.ConsultantStayForPatientCodingListVoCollection;
import ims.RefMan.vo.SearchCriteriaForPatientCodingListVo;
import ims.RefMan.vo.WardStayForPatientCodingListVo;
import ims.RefMan.vo.WardStayForPatientCodingListVoCollection;
import ims.core.admin.pas.vo.AdmissionDetailRefVo;
import ims.core.admin.pas.vo.AdmissionDetailRefVoCollection;
import ims.core.vo.lookups.CodingStatus;
import ims.framework.FormName;
import ims.framework.MessageButtons;
import ims.framework.MessageIcon;
import ims.framework.controls.DynamicGridCell;
import ims.framework.controls.DynamicGridColumn;
import ims.framework.controls.DynamicGridRow;
import ims.framework.enumerations.Alignment;
import ims.framework.enumerations.DialogResult;
import ims.framework.enumerations.DynamicCellType;
import ims.framework.enumerations.FormMode;
import ims.framework.enumerations.SortOrder;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.framework.utils.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Logic extends BaseLogic
{
	private static final long serialVersionUID = 1L;
	
	private static final Integer	COLUMN_CONSULTANT							= new Integer(0);
	
	private static final Integer	COLUMN_CODINGSTATUS							= new Integer(1);
	private static final Integer	COLUMN_SELECTED								= new Integer(2);
	private static final Integer	COLUMN_COMMENTS								= new Integer(3);
	
	@Override
	protected void onFormOpen(Object[] args) throws PresentationLogicException
	{
		form.lnkReturnToList().setVisible(args!=null && args.length>0 && args[0] instanceof Boolean && Boolean.TRUE.equals((Boolean)args[0])); //WDEV-20786
		
		initialise();
		if( form.getGlobalContext().RefMan.getSearchCriteriaForPatientCodingListVoIsNotNull() )
			refreshSearchCriteria();
		else
			searchandValidation();
			
		
	}
	private void createDyngrdPatientCodingList()
	{
		form.dyngrdPatientCodingList().getRows().clear();
		form.dyngrdPatientCodingList().setHeaderHeight(50);
		
		DynamicGridColumn columnName = form.dyngrdPatientCodingList().getColumns().newColumn("Admission", COLUMN_CONSULTANT);
		columnName.setCanGrow(true);
		columnName.setWidth(570);
		
		columnName = form.dyngrdPatientCodingList().getColumns().newColumn("Coding Status", COLUMN_CODINGSTATUS);
		columnName.setCanGrow(true);
		columnName.setWidth(130);
		
		columnName = form.dyngrdPatientCodingList().getColumns().newColumn("Selected", COLUMN_SELECTED);
		columnName.setCanGrow(true);
		columnName.setHeaderAlignment(Alignment.CENTER);
		columnName.setAlignment(Alignment.CENTER);
		columnName.setWidth(50);
		
		columnName = form.dyngrdPatientCodingList().getColumns().newColumn("Comments", COLUMN_COMMENTS);
		columnName.setCanGrow(true);
		columnName.setAlignment(Alignment.CENTER);
		columnName.setHeaderAlignment(Alignment.CENTER);
		columnName.setWidth(-1);
		
	}
	private void refreshSearchCriteria() 
	{
	
		SearchCriteriaForPatientCodingListVo filterVo = form.getGlobalContext().RefMan.getSearchCriteriaForPatientCodingListVo();
		if( filterVo == null )
			return;
		
		form.dteDateFrom().setValue(filterVo.getDateFrom());
		form.dteDateTo().setValue(filterVo.getDateTo());
		form.chkInProgress().setValue(filterVo.getInProgress());
		form.chkUncoded().setValue(filterVo.getUncoded());
		form.chkForReview().setValue(filterVo.getForReview());
		form.chkCoded().setValue(filterVo.getCoded());
		searchandValidation();
		
	}
	private void searchandValidation()
	{
		if( isSearchCriteriaValid() == false)
			return;
				
		if( search())
			form.getGlobalContext().RefMan.setSearchCriteriaForPatientCodingListVo(populateSearchCriteriaDataFromScreen());
	}
	private SearchCriteriaForPatientCodingListVo populateSearchCriteriaDataFromScreen()
	{
		SearchCriteriaForPatientCodingListVo  criteria = new SearchCriteriaForPatientCodingListVo();
		criteria.setDateFrom(form.dteDateFrom().getValue());
		criteria.setDateTo(form.dteDateTo().getValue());
		criteria.setInProgress(form.chkInProgress().getValue());
		criteria.setUncoded(form.chkUncoded().getValue());
		criteria.setForReview(form.chkForReview().getValue());
		criteria.setCoded(form.chkCoded().getValue());
		//criteria.setAwaitingHistology(form.chkAwaitingHistology().getValue());
			
		return criteria;
	}
	private boolean isSearchCriteriaValid()
	{
		Date dateFrom = form.dteDateFrom().getValue();
		Date dateTo = form.dteDateTo().getValue();

		if (dateFrom != null && dateTo != null && dateTo.isLessThan(dateFrom))
		{
			engine.showMessage("Date From cannot be greater than Date To", "Validation error", MessageButtons.OK, MessageIcon.ERROR);
			return false;
		}

		return true;
	}
	
	private boolean search() //WDEV-20075
	{
		form.dyngrdPatientCodingList().getRows().clear();
		
	 	ArrayList listAll = domain.getAdmissionDetailVo(populateSearchCriteriaDataFromScreen(),form.getGlobalContext().Core.getPatientShort());
		if( listAll == null ||  listAll.size() == 0 )
		{
			engine.showMessage("No records that match the search criteria entered were found.", "No Data Found", MessageButtons.OK,MessageIcon.INFORMATION); 
			return false;
		}
		
		Collections.sort(listAll, new DateTimeComparator(SortOrder.DESCENDING));
		
		populateGrid(listAll);
		
		return true;
	}
	private void populateGrid( ArrayList recordColl) //WDEV-20075
	{
		if( recordColl == null )
			return;
		
		if( recordColl != null )
		{
    		for(int i = 0; i < recordColl.size();i++ )
    		{
    			addRowToGrid(recordColl.get(i));
    		}
		}
	
	}
	
	private void addRowToGrid(Object recordObj/*AdmissionDetailForPatientCodingListVo record*/)//WDEV-20075
	{
		if( recordObj == null )
			return;
		
		if( recordObj instanceof AdmissionDetailsWithConsultantAndWardStayVo)
		{
			addAdmissionDetailsRow((AdmissionDetailsWithConsultantAndWardStayVo) recordObj);
		}
		else if (recordObj instanceof BookingAppointmentForPatientCodingListVo)
		{
			addOutpatientDetailsRow((BookingAppointmentForPatientCodingListVo)recordObj);
		}
	}
	
	private void addOutpatientDetailsRow(BookingAppointmentForPatientCodingListVo appointment)
	{
		if (appointment==null)
			return;
		
		DynamicGridRow newRow = form.dyngrdPatientCodingList().getRows().newRow();
		newRow.setCollapsedImage(form.getImages().RefMan.Clinic24);
		newRow.setExpandedImage(form.getImages().RefMan.Clinic24);
		
		DynamicGridCell rowCell = newRow.getCells().newCell(getColumn(COLUMN_CONSULTANT), DynamicCellType.STRING);
		rowCell.setReadOnly(true);
		
		String outpatientText = "";
		outpatientText+=appointment.getSessionIsNotNull() && appointment.getSession().getNameIsNotNull() ? "<b>Clinic: </b>" + appointment.getSession().getName() : "";
		outpatientText+=appointment.getSessionIsNotNull() && appointment.getSessionSlotIsNotNull() &&  appointment.getSessionSlot().getSlotRespIsNotNull() && appointment.getSessionSlot().getSlotResp().getHcpIsNotNull()&& appointment.getSessionSlot().getSlotResp().getHcp().getMosIsNotNull()? " - " + appointment.getSessionSlot().getSlotResp().getHcp().getMos().getName() : "";
		outpatientText+=appointment.getSessionIsNotNull() && appointment.getSession().getLocationIsNotNull() ? " - " + appointment.getSession().getLocation().getName() : "";
		outpatientText+=appointment.getAppointmentDateIsNotNull() ? " - <b>Appointment Date: </b>" + appointment.getAppointmentDate() : "";
		rowCell.setValue(outpatientText);
		rowCell.setTooltip(outpatientText);
	
		newRow.setValue(appointment);
	}
	
	private void addFCERow(DynamicGridRow newRow, ConsultantStayForPatientCodingListVo consultantStay)
	{
		if (consultantStay==null)
			return;
		
		if ((Boolean.TRUE.equals(form.chkUncoded().getValue()) && !Boolean.TRUE.equals(form.chkInProgress().getValue()) && !Boolean.TRUE.equals(form.chkForReview().getValue()) && !Boolean.TRUE.equals(form.chkCoded().getValue())) && !(consultantStay.getCodingStatus()==null || CodingStatus.UNCODED.equals(consultantStay.getCodingStatus())))
		{
			return;
		}
		
		DynamicGridRow newRowChild = newRow.getRows().newRow();
		newRowChild.setCollapsedImage(form.getImages().RefMan.Consultant24);
		newRowChild.setExpandedImage(form.getImages().RefMan.Consultant24);
		
		DynamicGridCell rowCell = newRowChild.getCells().newCell(getColumn(COLUMN_CONSULTANT), DynamicCellType.STRING);
		rowCell.setReadOnly(true);
		
		String consText = "";
		consText+=consultantStay.getConsultant() != null  && consultantStay.getConsultant().getMos() != null && consultantStay.getConsultant().getMos().getNameIsNotNull() ? "<b>Consultant: </b>"+consultantStay.getConsultant().getMos().getName().toString():"";
		consText+=consultantStay.getServiceIsNotNull()? " - <b>Service: </b>"+consultantStay.getService().getServiceName():""; //WDEV-20395
		consText+=consultantStay.getConsultant()!=null && consultantStay.getConsultant().getSpecialty() != null  ? " - <b>Cons Specialty: </b>"+consultantStay.getConsultant().getSpecialty().getText():"";//WDEV-21234 //WDEV-23905
		consText+=consultantStay.getTransferDateTimeIsNotNull() ? " - <b>Start Date: </b>"+consultantStay.getTransferDateTime() :"";
		consText+=consultantStay.getEndDateTimeIsNotNull() ? " - <b>End Date: </b>"+consultantStay.getEndDateTime() :"";
		
		rowCell.setValue(consText);
		rowCell.setTooltip(consText);
		
		addFCECodingStatusCell(consultantStay, newRowChild);
		
		newRowChild.setValue(consultantStay);
	}
	
	private void addWardStayRow(DynamicGridRow newRow, WardStayForPatientCodingListVo wardStay)
	{
		if (wardStay==null )
		{
			return;
		}
		
		DynamicGridRow newRowChild = newRow.getRows().newRow();
		newRowChild.setCollapsedImage(form.getImages().RefMan.Ward24);
		newRowChild.setExpandedImage(form.getImages().RefMan.Ward24);
		
		DynamicGridCell rowCell = newRowChild.getCells().newCell(getColumn(COLUMN_CONSULTANT), DynamicCellType.STRING);
		rowCell.setReadOnly(true);
		
		String wardText = "";
		wardText+=wardStay.getWardIsNotNull() && wardStay.getWard().getNameIsNotNull() ? "<b>Ward: </b>" + wardStay.getWard().getName():"";//WDEV-21345
		wardText+=wardStay.getTransferDateTime() != null ? " - <b>Start Date: </b>" + wardStay.getTransferDateTime() :"";
		wardText+=wardStay.getTransferOutDateTime() != null ? " - <b>End Date: </b>" + wardStay.getTransferOutDateTime() :"";
		
		rowCell.setValue(wardText);
		rowCell.setTooltip(wardText);
	
		newRowChild.setValue(wardStay);
	
	}
	private void addAdmissionDetailsRow(AdmissionDetailsWithConsultantAndWardStayVo recordWC) //WDEV-22922
	{
		if( recordWC!=null && recordWC.getAdmissionDetail()!= null)
		{
    		AdmissionDetailForPatientCodingListVo record = recordWC.getAdmissionDetail();
    		
    		DynamicGridRow newRow = form.dyngrdPatientCodingList().getRows().newRow();
    		newRow.setCollapsedImage(form.getImages().RefMan.Admission24);
    		newRow.setExpandedImage(form.getImages().RefMan.Admission24);
    		newRow.setValue(record);
    		
    		DynamicGridCell rowCell = newRow.getCells().newCell(getColumn(COLUMN_CONSULTANT), DynamicCellType.STRING);
    		rowCell.setReadOnly(true);
    		
    		String admissionText = "";
    		admissionText=admissionText+(record.getAdmissionDateTime() != null ? "<b>Admission Date: </b>" + record.getAdmissionDateTime() : "" );
    		admissionText=admissionText+(recordWC.getDischargeEpisode()!=null && recordWC.getDischargeEpisode().getDischargeDateTimeIsNotNull() ? " - <b>Discharge Date: </b>" + recordWC.getDischargeEpisode().getDischargeDateTime() : "");
    		rowCell.setValue(admissionText);
    		rowCell.setTooltip(admissionText);
    	
    		addCodingStatusAndSelectedCell(record, newRow);
    		addComentsCell(record, newRow);
    		
    		if (recordWC.getDischargeEpisode()==null)
    		{
    			newRow.setBackColor(Color.LightGreen);
    		}
    		newRow.setExpanded(true);
    		
    		ConsultantStayForPatientCodingListVoCollection collConsultantStays = new ConsultantStayForPatientCodingListVoCollection();
    		WardStayForPatientCodingListVoCollection collWardstays = new WardStayForPatientCodingListVoCollection();
    		
    		if( recordWC.getInpatientEpisodeIsNotNull())
    		{
    			//Populate Finished Consultant Episodes
    			if( recordWC.getInpatientEpisode().getConsultantStaysIsNotNull() && recordWC.getInpatientEpisode().getConsultantStays().size() > 0)
    			{
    				for(int k = 0; k < recordWC.getInpatientEpisode().getConsultantStays().size();k++)
    				{
    					collConsultantStays.add(recordWC.getInpatientEpisode().getConsultantStays().get(k));
    				}
    			}
    			
    			//Populate Ward Stays
    			if( recordWC.getInpatientEpisode().getWardStaysIsNotNull() && recordWC.getInpatientEpisode().getWardStays().size() > 0 )
    			{
    				for(int m = 0; m < recordWC.getInpatientEpisode().getWardStays().size();m++)
    				{
    					collWardstays.add(recordWC.getInpatientEpisode().getWardStays().get(m));
    				}
    			}
    			
    		}
    		else if( recordWC.getDischargeEpisodeIsNotNull() )
    		{
    			if( recordWC.getDischargeEpisode().getConsultantStaysIsNotNull() && recordWC.getDischargeEpisode().getConsultantStays().size() > 0)
    			{
    				//Populate Finished Consultant Episodes
    				for(int k = 0; k < recordWC.getDischargeEpisode().getConsultantStays().size();k++)
    				{
    					collConsultantStays.add(recordWC.getDischargeEpisode().getConsultantStays().get(k));
    				}
    			}
    			if( recordWC.getDischargeEpisode().getWardStaysIsNotNull() && recordWC.getDischargeEpisode().getWardStays().size() > 0 )
    			{
    				//Populate Ward Stays
    				for(int m = 0; m < recordWC.getDischargeEpisode().getWardStays().size();m++)
    				{
    					collWardstays.add(recordWC.getDischargeEpisode().getWardStays().get(m));
    				}
    			}
    		}
    		
    		//WDEV-22922
    		collConsultantStays.sort(SortOrder.ASCENDING);
    		for(int i = 0; collConsultantStays!=null && i < collConsultantStays.size();i++)
			{
    			addFCERow(newRow, collConsultantStays.get(i));
			}
    		
    		collWardstays.sort(SortOrder.ASCENDING);
    		for(int i = 0; collWardstays!=null && i < collWardstays.size();i++)
			{
				addWardStayRow(newRow, collWardstays.get(i));	
			}
		}
	}
	private void addComentsCell(AdmissionDetailForPatientCodingListVo record, DynamicGridRow newRow)
	{
		DynamicGridCell rowCell;
		if( record.getCodingCommentsIsNotNull() &&  record.getCodingComments().size()  > 0)
		{
			String strTooltip = null;
			String strFreeText = null;
			String structured = null;
			rowCell = newRow.getCells().newCell(getColumn(COLUMN_COMMENTS), DynamicCellType.IMAGEBUTTON);
			rowCell.setReadOnly(false);
			rowCell.setValue(form.getImages().Core.Comment16);
			rowCell.setAutoPostBack(true);
			
			
			for(int h = 0; h < record.getCodingComments().size();h++)
			{
			
				if( record.getCodingComments().get(h).getCommentText() == null && record.getCodingComments().get(h).getStructuredComment() == null )
					continue;
				
				strFreeText = "<b>Comment: </b>" + (record.getCodingComments().get(h).getCommentText() != null ? record.getCodingComments().get(h).getCommentText():"");
				structured =  "<b>Structured Comments: </b>" + (record.getCodingComments().get(h).getStructuredComment() != null ? record.getCodingComments().get(h).getStructuredComment().getIItemText(): "");
				
				if( strTooltip == null)
					strTooltip = strFreeText+ "<br>" + structured + "<br>";
				else
					strTooltip += strFreeText+ "<br>" + structured + "<br>";
			
			}
			rowCell.setTooltip(strTooltip);
			
		}
	}
	private void addCodingStatusAndSelectedCell(AdmissionDetailForPatientCodingListVo record, DynamicGridRow newRow)
	{
		DynamicGridCell rowCell;
		rowCell = newRow.getCells().newCell(getColumn(COLUMN_CODINGSTATUS), DynamicCellType.STRING);
		rowCell.setReadOnly(true);
		rowCell.setValue(record.getCodingStatusIsNotNull() ? record.getCodingStatus().getText():"");
		rowCell.setTooltip(record.getCodingStatusIsNotNull() ? record.getCodingStatus().getText():"");
	
		rowCell = newRow.getCells().newCell(getColumn(COLUMN_SELECTED), DynamicCellType.BOOL);
		rowCell.setAutoPostBack(true);
	}
	
	private void addFCECodingStatusCell(ConsultantStayForPatientCodingListVo consultantStay, DynamicGridRow newRow)
	{
		DynamicGridCell rowCell = newRow.getCells().newCell(getColumn(COLUMN_CODINGSTATUS), DynamicCellType.STRING);
		rowCell.setReadOnly(true);
		rowCell.setValue(consultantStay.getCodingStatusIsNotNull() ? consultantStay.getCodingStatus().getText():CodingStatus.UNCODED.getText());
		rowCell.setTooltip(consultantStay.getCodingStatusIsNotNull() ? consultantStay.getCodingStatus().getText():CodingStatus.UNCODED.getText());
	
	}
	
	private DynamicGridColumn getColumn(Integer identifier)
	{
		return form.dyngrdPatientCodingList().getColumns().getByIdentifier(identifier);
	}
	
	protected void onBtnCodeClick() throws ims.framework.exceptions.PresentationLogicException
	{
		startCode();
	}
	
	private void startCode()
	{
		AdmissionDetailRefVoCollection tempColl = new AdmissionDetailRefVoCollection();
		for(int i = 0; i < form.dyngrdPatientCodingList().getRows().size();i++)
		{
			DynamicGridRow row = form.dyngrdPatientCodingList().getRows().get(i);
			DynamicGridCell cellsel = row.getCells().get(getColumn(COLUMN_SELECTED));
			if( cellsel!=null && Boolean.TRUE.equals(cellsel.getValue()) )
			{
				tempColl.add((AdmissionDetailRefVo) row.getValue());
			}
			
		}
		engine.open(form.getForms().RefMan.ReferralExternalCoding, new Object[] {tempColl});
	}
	
	protected void onImbSearchClick() throws ims.framework.exceptions.PresentationLogicException
	{
		searchandValidation();
		
		refreshButtonsForSelection();
	}
	
	protected void onImbClearClick() throws ims.framework.exceptions.PresentationLogicException
	{
		clearScreen();
		form.getGlobalContext().RefMan.setSearchCriteriaForPatientCodingListVo(null);
		
		refreshButtonsForSelection();
	}
	private void clearScreen()
	{
		form.dteDateFrom().setValue(null);
		form.dteDateTo().setValue(null);
		form.chkInProgress().setValue(null);
		form.chkUncoded().setValue(null);
		form.chkForReview().setValue(null);
		form.chkCoded().setValue(null);
		//form.chkAwaitingHistology().setValue(null);
		form.dyngrdPatientCodingList().getRows().clear();
	}
	
	protected void onGrdPatientCodingListGridHeaderClicked(int column) throws ims.framework.exceptions.PresentationLogicException
	{
		
	}
	
	protected void onGrdPatientCodingListSelectionChanged()
	{
		// TODO Add your code here.
	}
	
	private void initialise()
	{
		
		form.dyngrdPatientCodingList().setSelectable(true);
		createDyngrdPatientCodingList();
		
		form.btnAddComent().setEnabled(false);
		form.btnCode().setEnabled(false);
		
		form.chkInProgress().setValue(true);
		form.chkUncoded().setValue(true);
		form.chkForReview().setValue(true);
		form.chkCoded().setValue(true);
		
		
	}
	
	protected void onBtnAddComentClick() throws PresentationLogicException
	{
		/*DynamicGridRow  row = form.dyngrdPatientCodingList().getSelectedRow();
		if(row != null )
		{
			form.getGlobalContext().RefMan.setAdmissionDetailRef((AdmissionDetailRefVo) row.getValue());
			engine.open(form.getForms().RefMan.CodingComment);
		}*/
		
		for(int i = 0; i < form.dyngrdPatientCodingList().getRows().size();i++)
		{
			DynamicGridRow row = form.dyngrdPatientCodingList().getRows().get(i);
			if( row != null )
			{
    			DynamicGridCell cellsel = row.getCells().get(getColumn(COLUMN_SELECTED));
    			if( cellsel!=null && Boolean.TRUE.equals(cellsel.getValue()) )
    			{
    				form.getGlobalContext().RefMan.setAdmissionDetailRef((AdmissionDetailRefVo) row.getValue());
    				engine.open(form.getForms().RefMan.CodingComment, new Object[] {FormMode.EDIT});
    				break;
    			}
			}
			
			
		}
		
	}
	
	protected void onDyngrdPatientCodingListCellButtonClicked(DynamicGridCell cell)
	{
		
		DynamicGridRow  row = cell.getRow();
		if(row != null )
		{
			form.getGlobalContext().RefMan.setAdmissionDetailRef((AdmissionDetailRefVo) row.getValue());
			engine.open(form.getForms().RefMan.CodingComment, new Object[] {FormMode.VIEW});
		}
	}
	
	protected void onDyngrdPatientCodingListRowChecked(DynamicGridRow row)
	{
		
		
	}
	
	protected void onDyngrdPatientCodingListCellValueChanged(DynamicGridCell cell)
	{
		if( cell.getColumn().equals(getColumn(COLUMN_SELECTED)))
		{
			refreshButtonsForSelection();
		}
		
	}
	private void refreshButtonsForSelection()
	{
		long grdrows = 0;
		
		for(int i = 0; i < form.dyngrdPatientCodingList().getRows().size();i++)
		{
			DynamicGridRow row = form.dyngrdPatientCodingList().getRows().get(i);
			DynamicGridCell cellsel = row.getCells().get(getColumn(COLUMN_SELECTED));
			if( cellsel!=null && Boolean.TRUE.equals(cellsel.getValue()) )
			{
				grdrows++;
			}
			if( grdrows > 1)
				break;
		}
		if( grdrows == 0 )
		{
			form.btnAddComent().setEnabled(false);
			form.btnCode().setEnabled(false);
		}
		if(grdrows > 0)
		{
			form.btnAddComent().setEnabled(true);
			form.btnCode().setEnabled(true);
		}
		if(grdrows > 1)
		{
			form.btnAddComent().setEnabled(false);
		}
	}
	protected void onFormDialogClosed(FormName formName, DialogResult result) throws PresentationLogicException
	{
		if( form.getForms().RefMan.CodingComment.equals(formName) )			
		{
			if(	DialogResult.OK.equals(result))
			{
				searchandValidation();
				refreshButtonsForSelection();
			}
		}
		
	}

	//WDEV-20075
	public class DateTimeComparator implements Comparator
	{
		private int direction = 1;
		
		public DateTimeComparator()
		{
			this(SortOrder.ASCENDING);
		}
		
		public DateTimeComparator(SortOrder order)
		{
			if (order == SortOrder.DESCENDING)
				direction = -1;
			
		}
		public int compare(Object ob1, Object ob2) 
		{
			DateTime dateTime1 = null;
			DateTime dateTime2 = null;
			
			if(ob1 instanceof AdmissionDetailsWithConsultantAndWardStayVo )
			{
				AdmissionDetailsWithConsultantAndWardStayVo vo1 = (AdmissionDetailsWithConsultantAndWardStayVo)ob1;
				dateTime1 = vo1!=null && vo1.getAdmissionDetailIsNotNull() ? vo1.getAdmissionDetail().getAdmissionDateTime() : null;
			}
			else if (ob1 instanceof BookingAppointmentForPatientCodingListVo)
			{
				BookingAppointmentForPatientCodingListVo vo1=(BookingAppointmentForPatientCodingListVo)ob1;
				dateTime1 = vo1.getAppointmentDateIsNotNull() ? new DateTime(vo1.getAppointmentDate(), new Time(0, 0, 0)): null;
			}
			
			if(ob2 instanceof AdmissionDetailsWithConsultantAndWardStayVo )
			{
				AdmissionDetailsWithConsultantAndWardStayVo vo2 = (AdmissionDetailsWithConsultantAndWardStayVo)ob2;
				dateTime2 = vo2!=null && vo2.getAdmissionDetailIsNotNull() ? vo2.getAdmissionDetail().getAdmissionDateTime() : null;
			}
			else if (ob2 instanceof BookingAppointmentForPatientCodingListVo)
			{
				BookingAppointmentForPatientCodingListVo vo2=(BookingAppointmentForPatientCodingListVo)ob2;
				dateTime2 = vo2.getAppointmentDateIsNotNull() ? new DateTime(vo2.getAppointmentDate(), new Time(0, 0, 0)): null;
			}
			
			if(dateTime1 != null )
				return  dateTime1.compareTo(dateTime2)*direction;
			if(dateTime2 != null)
				return (-1)*direction;
			
			return 0;
		}
	
	}

	//WDEV-20786
	@Override
	protected void onLnkReturnToListClick() throws PresentationLogicException
	{
		engine.open(form.getForms().RefMan.InpatientClinicalCodingWorklist);
	}
	
}
