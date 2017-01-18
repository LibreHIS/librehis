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
// This code was generated by Alexie Ursache using IMS Development Environment (version 1.55 build 2741.25619)
// Copyright (C) 1995-2007 IMS MAXIMS plc. All rights reserved.

package ims.ccosched.forms.doctorsworklist;

import ims.alerts.PatientAlertCCO;
import ims.ccosched.Helper.MultiColumnComparator;
import ims.ccosched.Helper.PatientShortHelper;
import ims.ccosched.Helper.ReportDataSourceBuilder;
import ims.ccosched.forms.doctorsworklist.GenForm.grdDoctorsListRow;
import ims.ccosched.vo.DoctorsWorkListItemVo;
import ims.ccosched.vo.DoctorsWorkListItemVoCollection;
import ims.core.vo.PatientShort;
import ims.domain.exceptions.DomainInterfaceException;
import ims.domain.exceptions.StaleObjectException;
import ims.dto.client.Clinical_team;
import ims.dto.client.Doctype;
import ims.dto.client.Lkup;
import ims.dto.client.Patient;
import ims.dto.client.Sd_appt_dts;
import ims.dto.client.Sd_appt_dts.Sd_appt_dtsRecord;
import ims.dto.client.Sd_comp_plan_lst;
import ims.dto.client.Sd_comp_plan_lst.Sd_comp_plan_lstRecord;
import ims.framework.FormName;
import ims.framework.enumerations.DialogResult;
import ims.framework.enumerations.SortOrder;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DateFormat;
import ims.framework.utils.Time;
import ims.framework.utils.TimeFormat;
import ims.framework.utils.beans.ColorBean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ims.printserver.PrintServer;

public class Logic extends BaseLogic
{
	private static final String	NICE_PINK							= "#FFCCCC";
	private static final String	NICE_PURPLE							= "#CCCCFF";
	private static final String	NICE_GREEN							= "#D6F4FE";
	private static final int	ALL									= -453;
	public static final int		FLOOR_CLINICAL_APPOINTMENTS			= -450;
	public static final int		PLANNING_ACTIVITY_MOULD_SIMULATION	= -451;
	public static final int		PRETREATMENT_ACTIVITY				= -452;
	
	//private static final int	MOULD_PRODUCTION = -114;
	//private static final int	FLOOR_CLINIC = -112;

	@Override
	protected void onFormOpen() throws ims.framework.exceptions.PresentationLogicException
	{
		initialize();
	}
	
	@Override
	protected void onBtnPrintClick() throws ims.framework.exceptions.PresentationLogicException
	{
		if(form.grdDoctorsList().getRows().size() == 0)
		{
			engine.showMessage("There are no records to print !");
			return;
		}
		
		engine.open(form.getForms().CcoSched.PrinterSelect);
	}
	
	@Override
	protected void onBtnSearchClick() throws ims.framework.exceptions.PresentationLogicException
	{
		doSearch();
		displayFooter(form.grdDoctorsList().getRows().size());
	}
	
	@Override
	protected void onGrdDoctorsListSelectionChanged() throws PresentationLogicException
	{
		setPID();
		setPIDTooltip();
		if(form.grdDoctorsList().getValue() instanceof Sd_comp_plan_lstRecord)
		{
			form.getGlobalContext().CcoSched.ComputerPlanning.setPlanDto((Sd_comp_plan_lstRecord)form.grdDoctorsList().getValue());
			engine.open(form.getForms().CcoSched.PretreatmentUpdate);
		}
	}
	
	private void setPIDTooltip()
	{
		engine.clearAlertsByType(ims.alerts.PatientAlertCCO.class);
		engine.addAlert(new ims.alerts.PatientAlertCCO(new ims.ccosched.Helper.PIDTooltip().getTooltip(form.getGlobalContext().Core.getPatientShort(), null, null)));
	}

	private void setPID()
	{
		Object record = form.grdDoctorsList().getValue();
		String pkey = null;
		if(record instanceof Sd_comp_plan_lstRecord)
			pkey = ((Sd_comp_plan_lstRecord)record).Pkey;
		else if(record instanceof Sd_appt_dtsRecord)
			pkey = ((Sd_appt_dtsRecord)record).Pkey;
		
		ims.dto.client.Patient patient;
		try
		{
			patient = domain.getPatient(pkey);
		}
		catch (DomainInterfaceException e)
		{
			engine.showMessage(e.getMessage());
			return;
		}
		
		if(patient != null)
			setPatientIntoContext(patient.DataCollection.get(0));
	}
	private void setPatientIntoContext(Patient.PatientRecord patient)
	{
		PatientShort patientShort = new PatientShortHelper().getPatientShort(patient);
		
		ims.core.vo.Patient voPatient = null;

		if(patientShort != null)
		{
			try
			{
				voPatient = domain.getPatient(patientShort);
			}
			catch (StaleObjectException e)
			{
				engine.showMessage(e.getMessage());
			}
			catch(DomainInterfaceException exc)
			{
				engine.showMessage(exc.getMessage());
			}
		}
		
		form.getGlobalContext().Core.setPatientShort(voPatient);
	}

	@Override
	protected void onFormDialogClosed(FormName formName, DialogResult result) throws PresentationLogicException
	{
		if(formName.equals(form.getForms().CcoSched.PretreatmentUpdate) && result.equals(DialogResult.OK))
		{
			doSearch();
		}
		
		if(formName.equals(form.getForms().CcoSched.PrinterSelect) && result.equals(DialogResult.OK))
		{
			String docType = "-212";
			
/*			if(form.grdDoctorsList().getValue() instanceof Sd_appt_dtsRecord)
			{
				
				Sd_appt_dtsRecord record = (Sd_appt_dtsRecord)form.grdDoctorsList().getValue();
				DoctorsWorkListFloorClinicRds obj = new DoctorsWorkListFloorClinicRds(domain.getDtoDomain(), record.Appt_id, record.Appt_head_id);
				str = obj.GetData();
				docType = "-214";
			}
			else if(form.grdDoctorsList().getValue() instanceof Sd_comp_plan_lstRecord)
			{
				Sd_comp_plan_lstRecord record = (Sd_comp_plan_lstRecord)form.grdDoctorsList().getValue();
				DoctorsWorkListRds obj = new DoctorsWorkListRds(domain.getDtoDomain(), record.Sd_comp_plan_id);
				str = obj.GetData();
				docType = "-212";
			}
*/
			ReportDataSourceBuilder ds = new ReportDataSourceBuilder();
			
			for (int i = 0; i < form.grdDoctorsList().getRows().size(); i++)
			{
				grdDoctorsListRow row = form.grdDoctorsList().getRows().get(i);
				
				ds.addField("HOSP", row.getcolHospNo());
				ds.addField("NAME", row.getColName());
				ds.addField("CLINIC", row.getcolClinicActivity());
				ds.addField("APPTIME", row.getColApptTime());
				ds.addField("ACTION", row.getColTreatmentAction());
				ds.addField("DEADLINE", (row.getColDeadLine() == null ? "" : row.getColDeadLine().toString()));
			}

			ds.addField("_PKEY_", "");
			ds.addField("_HOSPNUM_", "");
			ds.addField("_EPISODE_", "");
			
			StringBuffer sb = new StringBuffer();
			if(form.dteDate().getValue() != null)
				sb.append(form.dteDate().getValue().toString());
			sb.append(" - ");
			if(form.dteToDate().getValue() != null)
				sb.append(form.dteToDate().getValue().toString());
			
			ds.addField("INTERVAL", sb.toString());
			
			if(form.cmbHCP().getValue() != null)
			{
				ds.addField("HCP", getHcpText(Integer.parseInt(form.cmbHCP().getValue())));	
			}
			
			if(form.cmbActivity().getValue() != null)
			{
				ds.addField("ACTIVITY", getActivityText(Integer.parseInt(form.cmbActivity().getValue())));
			}
			
			String str = ds.buildDataSource();
			
			Doctype doctype = null;
			
			try
			{
				doctype = domain.getDoctype(docType);
			}
			catch (DomainInterfaceException e)
			{
				engine.showMessage(e.getMessage());
				return;
			}
			
			int nLeftMargin = 10;
			int nTopMargin = 10;
			int nRightMargin = 10;
			int nBottomMargin = 10;
			int nPaperSize = 9; //A4
			boolean bPortrait = true;
			
			try
			{
				nLeftMargin = Integer.parseInt(doctype.DataCollection.get(0).Leftmargin);
			} 
			catch (Exception e)
			{
				nLeftMargin = 10;
			}
			try
			{
				nTopMargin = Integer.parseInt(doctype.DataCollection.get(0).Topmargin);
			} 
			catch (Exception e)
			{
				nTopMargin = 10;
			}
			try
			{
				nRightMargin = Integer.parseInt(doctype.DataCollection.get(0).Rightmargin);
			} 
			catch (Exception e)
			{
				nRightMargin = 10;
			}
			try
			{
				nBottomMargin = Integer.parseInt(doctype.DataCollection.get(0).Bottommargin);
			} 
			catch (Exception e)
			{
				nBottomMargin = 10;
			}
			try
			{
				nPaperSize = Integer.parseInt(doctype.DataCollection.get(0).Papersize);
			} 
			catch (Exception e)
			{
				nPaperSize = 9; //A4
			}

			bPortrait = doctype.DataCollection.get(0).Orientation.equals("1") ? true : false;
			
	        try
			{
	        	PrintServer.PrintDocument(doctype.DataCollection.get(0).Tmpl_content.getBytes(), str, form.getGlobalContext().CcoSched.getSelectedPrinter(), bPortrait, nLeftMargin, nTopMargin, nRightMargin, nBottomMargin, nPaperSize, false);
			}
	        catch(Exception e)
			{
	        	engine.showMessage(e.getMessage());
			}
			
			return;
		}
	}
	
	private void doSearch()
	{
		form.btnPrint().setEnabled(false);
		form.grdDoctorsList().getRows().clear();
		
		String[] errors = getUIErrors();
		if(errors != null)
		{
			engine.showErrors("Search criteria validation error", errors);
			return;
		}
		
		String item = form.cmbActivity().getValue();
		if(item != null)
		{
			int activityId = Integer.valueOf(item).intValue();
			
			//private static final int		ALL									= -453;
			//public static final int			FLOOR_CLINICAL_APPOINTMENTS			= -450;
			//public static final int			PLANNING_ACTIVITY_MOULD_SIMULATION	= -451;
			//public static final int			PRETREATMENT_ACTIVITY				= -452;

			//private static final int	SIMULATION = -115;
			//private static final int	MOULD_PRODUCTION = -114;
			//private static final int	FLOOR_CLINIC = -112;

			if(activityId == FLOOR_CLINICAL_APPOINTMENTS)				
			{				
				Sd_appt_dts floorClinic = listSd_appt_dts(FLOOR_CLINICAL_APPOINTMENTS);
				DoctorsWorkListItemVoCollection voColl = getVoFromDto(new DoctorsWorkListItemVoCollection(), floorClinic, FLOOR_CLINICAL_APPOINTMENTS);
				
				form.getLocalContext().setGridContent(voColl);
				populateGrid(voColl);
			}
			else if (activityId == PLANNING_ACTIVITY_MOULD_SIMULATION)
			{
				
				Sd_appt_dts mouldProd = listSd_appt_dts(PLANNING_ACTIVITY_MOULD_SIMULATION);
				DoctorsWorkListItemVoCollection voColl = getVoFromDto(new DoctorsWorkListItemVoCollection(), mouldProd, PLANNING_ACTIVITY_MOULD_SIMULATION);				
				
				form.getLocalContext().setGridContent(voColl);
				populateGrid(voColl);
			}
			else
			{
				if(activityId == PRETREATMENT_ACTIVITY)
				{
					Sd_comp_plan_lst pretreatment = listSd_comp_plan_lst();
					
					DoctorsWorkListItemVoCollection voFromDto = getVoFromDto(pretreatment, activityId);
					
					form.getLocalContext().setGridContent(voFromDto);
					populateGrid(voFromDto);
				}
				else if(activityId == ALL)
				{
					//All - do a list of all and sort it by date
					Sd_comp_plan_lst pretreatment = listSd_comp_plan_lst();
					DoctorsWorkListItemVoCollection voColl = getVoFromDto(pretreatment, PRETREATMENT_ACTIVITY);
					
					Sd_appt_dts floorClinic = listSd_appt_dts(FLOOR_CLINICAL_APPOINTMENTS);
					voColl = getVoFromDto(voColl, floorClinic, FLOOR_CLINICAL_APPOINTMENTS);

					floorClinic = listSd_appt_dts(PLANNING_ACTIVITY_MOULD_SIMULATION);
					voColl = getVoFromDto(voColl, floorClinic, PLANNING_ACTIVITY_MOULD_SIMULATION);

					if(voColl != null)
						voColl.sort(SortOrder.ASCENDING);
					
					form.getLocalContext().setGridContent(voColl);
					populateGrid(voColl);
				}
			}
		}
		
		int gridSize = form.grdDoctorsList().getRows().size();
		
		if(gridSize == 0)
		{
			engine.showMessage("No records found for the search criteria provided");
		}
		
		form.btnPrint().setEnabled(gridSize > 0);
	}
	private void displayFooter(int gridSize)
	{
		String squareBox = "&#9632;";//unicode character - squared box
		String legendClinicalAppointments = "<font color='" + getColour(FLOOR_CLINICAL_APPOINTMENTS)+ "' size='4'>" + squareBox + "</font>";
		String activityClinicalAppointments = getActivityText(FLOOR_CLINICAL_APPOINTMENTS);
		
		String legendMouldSimulation = "<font color='" + getColour(PLANNING_ACTIVITY_MOULD_SIMULATION)+ "' size='4'>" + squareBox + "</font>";
		String activityMouldSimulation = getActivityText(PLANNING_ACTIVITY_MOULD_SIMULATION);
		
		String legendPretreatmentActivity = "<font color='" + getColour(PRETREATMENT_ACTIVITY)+ "' size='4'>" + squareBox + "</font>";
		String activityPretreatmentActivity = getActivityText(PRETREATMENT_ACTIVITY);

		StringBuilder footer = new StringBuilder();
		footer.append("(" + gridSize + ")" + " record" + (gridSize>1?"s":""));
		footer.append("&nbsp&nbsp&nbsp&nbsp");
		footer.append(legendClinicalAppointments);
		footer.append(" ");
		footer.append(activityClinicalAppointments);
		
		footer.append("&nbsp&nbsp");
		footer.append(legendMouldSimulation);
		footer.append(" ");
		footer.append(activityMouldSimulation);
		
		footer.append("&nbsp&nbsp");
		footer.append(legendPretreatmentActivity);
		footer.append(" ");
		footer.append(activityPretreatmentActivity);
		
		form.grdDoctorsList().setFooterValue(footer.toString());
	}
	private String[] getUIErrors()
	{
		ArrayList<String> values = new ArrayList<String>(2);

		if (form.cmbHCP().getValue() == null)
		{
			form.cmbHCP().setFocus();
			values.add("HCP is mandatory");
		}

		if (form.cmbActivity().getValue() == null)
		{
			form.cmbActivity().setFocus();
			values.add("Activity is mandatory");
		}
		//wdev-12460
		
		if (form.dteDate().getValue() != null && form.dteToDate().getValue() != null)
		{
			if (form.dteDate().getValue().isGreaterThan(form.dteToDate().getValue()))
			{
				values.add("The \"Date\" is after the \"To Date\"");
				

			}
		}
		//---------------

		if (values.size() > 0)
		{
			String[] errors = new String[values.size()];
			return values.toArray(errors);
		}

		return null;
	}
	private Sd_comp_plan_lst listSd_comp_plan_lst()
	{
		//Pre-Treatment activity
		String act_consult = form.cmbHCP().getValue();
		String targetDate = getTargetDate();
		String activdone = "C";
		String requiresconsultant = "Y";

		try
		{
			return domain.listComputerPlanning(act_consult, requiresconsultant, targetDate, activdone);
		}
		catch (DomainInterfaceException e)
		{
			engine.showMessage(e.getMessage());
			return null;
		}
	}
	private String getTargetDate()
	{
		Date fromDate = form.dteDate().getValue();
		Date toDate   = form.dteToDate().getValue();
		
		if (fromDate != null && toDate == null)
		{
			return fromDate.toString(DateFormat.ISO);
		}
		else if (fromDate == null && toDate != null)
		{
			return "<" + toDate.toString(DateFormat.ISO);
		}
		else if (fromDate != null && toDate != null)
		{
			return fromDate.toString(DateFormat.ISO) + "|" + toDate.toString(DateFormat.ISO);
		}
		return "";
	}
	
	private DoctorsWorkListItemVoCollection getVoFromDto(Sd_comp_plan_lst pretreatment, int activityId)
	{
		if(pretreatment == null)
			return null;
		
		DoctorsWorkListItemVoCollection voColl = new DoctorsWorkListItemVoCollection();
		for (int i = 0; i < pretreatment.DataCollection.count(); i++)
		{
			Sd_comp_plan_lstRecord record = pretreatment.DataCollection.get(i);
			DoctorsWorkListItemVo vo = new DoctorsWorkListItemVo();
			vo.setID(record.Sd_comp_plan_id);
			vo.setHospNum(record.Hospnum);
			vo.setName(getName(record));
			//vo.setType(getActivityType(activityId));
			vo.setTypeID(new Integer(activityId));
			vo.setActivity(record.Planactivitytxt);
			vo.setHCP(record.Planhcptxt);
			vo.setClinic(record.Planactivitytxt);
			vo.setTreatmentAction(record.Tpactiontxt);
			vo.setAppointmentRecord(record);
			//vo.setAppointmentTime(record); - Not Applicable
			if(record.Targetdate != null && record.Targetdate.length() > 0)
			{
				try {vo.setDeadline(new Date(record.Targetdate, DateFormat.ISO));}catch (ParseException e){e.printStackTrace();}	
			}
			
			voColl.add(vo);
		}
		
		return voColl;
	}
	
	private String getName(Sd_comp_plan_lstRecord record)
	{
		StringBuilder name = new StringBuilder();
		if (record != null)
		{
			name.append(record.Pat_forname);
			if (name.length() > 0)
				name.append(" ");
			name.append(record.Pat_surname);

			return name.toString();
		}

		return "";
	}

	private DoctorsWorkListItemVoCollection getVoFromDto(DoctorsWorkListItemVoCollection voColl, Sd_appt_dts floorClinic, int activityId)
	{
		if(voColl == null || floorClinic == null)
			return null;
		
		for (int i = 0; i < floorClinic.DataCollection.count(); i++)
		{
			Sd_appt_dtsRecord record = floorClinic.DataCollection.get(i);
			
			DoctorsWorkListItemVo vo = new DoctorsWorkListItemVo();
			vo.setID(vo.getID());
			vo.setTypeID(new Integer(activityId));
			vo.setHospNum(record.Hospnum);
			vo.setName(record.Titl + " " + record.Snm + " " + record.Fnm1);
			vo.setActivity(record.Activitytxt);
			//vo.setType(getActivityType(activityId));
			vo.setHCP(record.Act_consulttxt);
			vo.setClinic(record.Prfile_sess_idtxt);
			vo.setAppointmentTime(getAppointmentTime(record));
			vo.setTreatmentAction(record.Action_idtxt);
			if(record.Appt_dt != null && record.Appt_dt.length() > 0)
				try {vo.setDeadline(new Date(record.Appt_dt, DateFormat.ISO));}catch (ParseException e){e.printStackTrace();}
			vo.setAppointmentRecord(record);
			voColl.add(vo);
		}
		
		return voColl;
	}
	private String getAppointmentTime(Sd_appt_dtsRecord record)
	{
		if(record != null)
		{
			Time startTime = getTimeFromString(record.Stm);
			Time endTime   = getTimeFromString(record.Etm);
			return startTime.toString(TimeFormat.DEFAULT) + " - " + endTime.toString(TimeFormat.DEFAULT);
		}
		return null;
	}
	
	private Time getTimeFromString(String time)
	{
		Time tm = null;
		if(time != null && !time.equals(""))
		try{ tm = new Time(time, TimeFormat.FLAT6);}catch(RuntimeException e){}
		return tm;
	}
	private Sd_appt_dts listSd_appt_dts(int activityId)
	{
		String act_consult = form.cmbHCP().getValue();
		String targetDate = getTargetDate();
		
		String grp_id = getGroupId(activityId);
		String activity_id = getActivityId(activityId);
		
		Sd_appt_dts floorClinic = null;
		try
		{
			floorClinic = domain.listAppointments(act_consult, targetDate, grp_id, activity_id);
		}
		catch (DomainInterfaceException e)
		{
			engine.showMessage(e.getMessage());
		}

		return floorClinic;
	}
	
	private String getActivityId(int activityId)
	{
		if (activityId == FLOOR_CLINICAL_APPOINTMENTS)
		{
			return "-112";
		}
		else if (activityId == PLANNING_ACTIVITY_MOULD_SIMULATION)
		{
			return "-114 || -115";
		}
		
		return "";
	}

	private String getGroupId(int activityId)
	{
		if (activityId == FLOOR_CLINICAL_APPOINTMENTS)
		{
			return "-101";
		}
		else if (activityId == PLANNING_ACTIVITY_MOULD_SIMULATION)
		{
			return "-103";
		}
		
		return "";
	}

	private void populateGrid(DoctorsWorkListItemVoCollection voColl)
	{
		form.grdDoctorsList().getRows().clear();
		
		for (int i = 0; voColl != null && i < voColl.size(); i++)
		{
			DoctorsWorkListItemVo vo = voColl.get(i);
			grdDoctorsListRow row = form.grdDoctorsList().getRows().newRow();
			
			addGridRow(vo, row);
		}
	}

	private void addGridRow(DoctorsWorkListItemVo vo, grdDoctorsListRow row)
	{
		row.setcolHospNo(vo.getHospNum());
		row.setColName(vo.getName());
		//row.setcolType(vo.getType());
		row.setcolClinicActivity(vo.getActivity());
		row.setColApptTime(vo.getAppointmentTime());
		row.setColTreatmentAction(vo.getTreatmentAction());
		row.setColDeadLine(vo.getDeadline());
		row.setBackColor(getColour(vo.getTypeID()));
		row.setValue(vo.getAppointmentRecord());
	}
	
	private Color getColour(Integer typeID)
	{
		if (typeID != null)
		{
			int activityId = typeID.intValue();
			if (activityId == FLOOR_CLINICAL_APPOINTMENTS)
			{
				ColorBean colourBean = new ColorBean();
				colourBean.setName(NICE_PURPLE);
				colourBean.setValue(NICE_PURPLE);
				return new Color(colourBean);
			}
			else if (activityId == PLANNING_ACTIVITY_MOULD_SIMULATION)
			{
				ColorBean colourBean = new ColorBean();
				colourBean.setName(NICE_PINK);
				colourBean.setValue(NICE_PINK);
				return new Color(colourBean);
			}
			else if (activityId == PRETREATMENT_ACTIVITY)
			{
				ColorBean colourBean = new ColorBean();
				colourBean.setName(NICE_GREEN);
				colourBean.setValue(NICE_GREEN);
				return new Color(colourBean);
			}
		}

		return Color.Default;
	}

	private void initialize()
	{
		form.dteDate().setValue(new Date());
		clearPIDAndPatient();
		loadLookups();
	}
	
	private void loadLookups()
	{
		form.btnPrint().setEnabled(false);
		Lkup lkup = null;
		try
		{
			lkup = domain.getLookupInstance("2000013");
		}
		catch (DomainInterfaceException e)
		{
			engine.showMessage(e.getMessage());
		}

		for (int i = 0; lkup != null && i < lkup.DataCollection.count(); i++)
		{
			form.cmbActivity().newRow(lkup.DataCollection.get(i).Lkup_id, lkup.DataCollection.get(i).Lkup_nm);
		}

		form.getLocalContext().setActivity(lkup);

		//HCP
		Clinical_team hcps = null;
		try
		{
			hcps = domain.listHCP("-100014");
		}
		catch (DomainInterfaceException e)
		{
			engine.showMessage(e.getMessage());
		}

		for (int i = 0; hcps != null && i < hcps.DataCollection.count(); i++)
		{
			form.cmbHCP().newRow(hcps.DataCollection.get(i).Hcp_id, hcps.DataCollection.get(i).Hcp_txt);
		}
		
		form.getLocalContext().setHcpList(hcps);
	}

	private void clearPIDAndPatient()
	{
		engine.clearAlertsByType(PatientAlertCCO.class);
		form.getGlobalContext().Core.setPatientShort(null);
	}
	
	private String getActivityText(int lookupId)
	{
		Lkup lkup = form.getLocalContext().getActivity();
		for (int i = 0; lkup != null && i < lkup.DataCollection.count(); i++)
		{
			if(Integer.valueOf(lkup.DataCollection.get(i).Lkup_id).intValue() == lookupId)
			{
				return lkup.DataCollection.get(i).Lkup_nm;
			}
		}
		
		return "";
	}

	private String getHcpText(int hcpId)
	{
		Clinical_team hcp = form.getLocalContext().getHcpList();
		for (int i = 0; hcp != null && i < hcp.DataCollection.count(); i++)
		{
			if(Integer.valueOf(hcp.DataCollection.get(i).Hcp_id).intValue() == hcpId)
			{
				return hcp.DataCollection.get(i).Hcp_txt;
			}
		}
		
		return "";
	}
	
	@Override
	protected void onGrdDoctorsListGridHeaderClicked(int column) throws PresentationLogicException
	{
		int[] colsToSort = new int[] {column + 1, 6};
		
		sortGrid(colsToSort);
	}
	
	@SuppressWarnings("unchecked")
	private void sortGrid(int[] colsToSort)
	{
		List items = new ArrayList();
		
		boolean[] ascendingOnCol = new boolean[colsToSort.length];
		
		for (int i = 0; i < colsToSort.length; i++)
		{
			ascendingOnCol[i] = true;
		}
		
		for (int i = 0; i < form.grdDoctorsList().getRows().size(); i++)
		{
			grdDoctorsListRow row = form.grdDoctorsList().getRows().get(i);
			Object[] item = new Object[7];
			
			item[0] = getWorkListItem(row.getValue());
			item[1] = row.getcolHospNo();
			item[2] = row.getColName() != null ? row.getColName().trim() : "";
			item[3] = row.getcolClinicActivity();
			item[4] = row.getColApptTime() != null ? row.getColApptTime() : "";
			item[5] = row.getColTreatmentAction();
			item[6] = row.getColDeadLine();
			
			items.add(item);
		}
		
		Collections.sort(items, new MultiColumnComparator(colsToSort, ascendingOnCol));

		populateGrid(items);
	}

	private Object getWorkListItem(Object value)
	{
		DoctorsWorkListItemVoCollection coll = form.getLocalContext().getGridContent();
		
		if(coll == null)
			return null;
		
		for (int i = 0; i < coll.size(); i++)
		{
			if(coll.get(i).getAppointmentRecord() != null && coll.get(i).getAppointmentRecord().equals(value))
				return coll.get(i);
		}
		
		return null;
	}

	private void populateGrid(List items)
	{
		form.grdDoctorsList().getRows().clear();
		for (int i = 0; i < items.size(); i++)
		{
			DoctorsWorkListItemVo record = (DoctorsWorkListItemVo)((Object[])items.get(i))[0];
			
			grdDoctorsListRow row = form.grdDoctorsList().getRows().newRow();
			
			addGridRow(record, row);
		}
	}
	
}
