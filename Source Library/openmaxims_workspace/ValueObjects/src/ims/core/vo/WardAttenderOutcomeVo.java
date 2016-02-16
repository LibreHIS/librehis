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

package ims.core.vo;

/**
 * Linked to Scheduling.Booking_Appointment business object (ID: 1055100007).
 */
public class WardAttenderOutcomeVo extends ims.scheduling.vo.Booking_AppointmentRefVo implements ims.vo.ImsCloneable, Comparable
{
	private static final long serialVersionUID = 1L;

	public WardAttenderOutcomeVo()
	{
	}
	public WardAttenderOutcomeVo(Integer id, int version)
	{
		super(id, version);
	}
	public WardAttenderOutcomeVo(ims.core.vo.beans.WardAttenderOutcomeVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.seenby = bean.getSeenBy() == null ? null : bean.getSeenBy().buildVo();
		this.outpatientprocedures = ims.RefMan.vo.PatientProcedureForAppointmentOutcomeVoCollection.buildFromBeanCollection(bean.getOutpatientProcedures());
		this.wardattendancehcptype = bean.getWardAttendanceHcpType() == null ? null : ims.core.vo.lookups.HcpDisType.buildLookup(bean.getWardAttendanceHcpType());
		this.wardattendanceoutcome = bean.getWardAttendanceOutcome() == null ? null : ims.core.vo.lookups.WardAttendanceOutcome.buildLookup(bean.getWardAttendanceOutcome());
		this.wardattendanceoutcomecomment = bean.getWardAttendanceOutcomeComment();
		this.apptstatus = bean.getApptStatus() == null ? null : ims.scheduling.vo.lookups.Status_Reason.buildLookup(bean.getApptStatus());
		this.session = bean.getSession() == null ? null : bean.getSession().buildVo();
		this.appointmentdate = bean.getAppointmentDate() == null ? null : bean.getAppointmentDate().buildDate();
		this.seentime = bean.getSeenTime() == null ? null : bean.getSeenTime().buildTime();
		this.apptstatushistory = ims.scheduling.vo.Appointment_StatusVoCollection.buildFromBeanCollection(bean.getApptStatusHistory());
		this.appttrackingstatushistory = ims.scheduling.vo.Appt_Tracking_Status_HistoryVoCollection.buildFromBeanCollection(bean.getApptTrackingStatusHistory());
		this.apptstarttime = bean.getApptStartTime() == null ? null : bean.getApptStartTime().buildTime();
		this.currentstatusrecord = bean.getCurrentStatusRecord() == null ? null : bean.getCurrentStatusRecord().buildVo();
	}
	public void populate(ims.vo.ValueObjectBeanMap map, ims.core.vo.beans.WardAttenderOutcomeVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.seenby = bean.getSeenBy() == null ? null : bean.getSeenBy().buildVo(map);
		this.outpatientprocedures = ims.RefMan.vo.PatientProcedureForAppointmentOutcomeVoCollection.buildFromBeanCollection(bean.getOutpatientProcedures());
		this.wardattendancehcptype = bean.getWardAttendanceHcpType() == null ? null : ims.core.vo.lookups.HcpDisType.buildLookup(bean.getWardAttendanceHcpType());
		this.wardattendanceoutcome = bean.getWardAttendanceOutcome() == null ? null : ims.core.vo.lookups.WardAttendanceOutcome.buildLookup(bean.getWardAttendanceOutcome());
		this.wardattendanceoutcomecomment = bean.getWardAttendanceOutcomeComment();
		this.apptstatus = bean.getApptStatus() == null ? null : ims.scheduling.vo.lookups.Status_Reason.buildLookup(bean.getApptStatus());
		this.session = bean.getSession() == null ? null : bean.getSession().buildVo(map);
		this.appointmentdate = bean.getAppointmentDate() == null ? null : bean.getAppointmentDate().buildDate();
		this.seentime = bean.getSeenTime() == null ? null : bean.getSeenTime().buildTime();
		this.apptstatushistory = ims.scheduling.vo.Appointment_StatusVoCollection.buildFromBeanCollection(bean.getApptStatusHistory());
		this.appttrackingstatushistory = ims.scheduling.vo.Appt_Tracking_Status_HistoryVoCollection.buildFromBeanCollection(bean.getApptTrackingStatusHistory());
		this.apptstarttime = bean.getApptStartTime() == null ? null : bean.getApptStartTime().buildTime();
		this.currentstatusrecord = bean.getCurrentStatusRecord() == null ? null : bean.getCurrentStatusRecord().buildVo(map);
	}
	public ims.vo.ValueObjectBean getBean()
	{
		return this.getBean(new ims.vo.ValueObjectBeanMap());
	}
	public ims.vo.ValueObjectBean getBean(ims.vo.ValueObjectBeanMap map)
	{
		ims.core.vo.beans.WardAttenderOutcomeVoBean bean = null;
		if(map != null)
			bean = (ims.core.vo.beans.WardAttenderOutcomeVoBean)map.getValueObjectBean(this);
		if (bean == null)
		{
			bean = new ims.core.vo.beans.WardAttenderOutcomeVoBean();
			map.addValueObjectBean(this, bean);
			bean.populate(map, this);
		}
		return bean;
	}
	public Object getFieldValueByFieldName(String fieldName)
	{
		if(fieldName == null)
			throw new ims.framework.exceptions.CodingRuntimeException("Invalid field name");
		fieldName = fieldName.toUpperCase();
		if(fieldName.equals("SEENBY"))
			return getSeenBy();
		if(fieldName.equals("OUTPATIENTPROCEDURES"))
			return getOutpatientProcedures();
		if(fieldName.equals("WARDATTENDANCEHCPTYPE"))
			return getWardAttendanceHcpType();
		if(fieldName.equals("WARDATTENDANCEOUTCOME"))
			return getWardAttendanceOutcome();
		if(fieldName.equals("WARDATTENDANCEOUTCOMECOMMENT"))
			return getWardAttendanceOutcomeComment();
		if(fieldName.equals("APPTSTATUS"))
			return getApptStatus();
		if(fieldName.equals("SESSION"))
			return getSession();
		if(fieldName.equals("APPOINTMENTDATE"))
			return getAppointmentDate();
		if(fieldName.equals("SEENTIME"))
			return getSeenTime();
		if(fieldName.equals("APPTSTATUSHISTORY"))
			return getApptStatusHistory();
		if(fieldName.equals("APPTTRACKINGSTATUSHISTORY"))
			return getApptTrackingStatusHistory();
		if(fieldName.equals("APPTSTARTTIME"))
			return getApptStartTime();
		if(fieldName.equals("CURRENTSTATUSRECORD"))
			return getCurrentStatusRecord();
		return super.getFieldValueByFieldName(fieldName);
	}
	public boolean getSeenByIsNotNull()
	{
		return this.seenby != null;
	}
	public ims.core.vo.HcpLiteVo getSeenBy()
	{
		return this.seenby;
	}
	public void setSeenBy(ims.core.vo.HcpLiteVo value)
	{
		this.isValidated = false;
		this.seenby = value;
	}
	public boolean getOutpatientProceduresIsNotNull()
	{
		return this.outpatientprocedures != null;
	}
	public ims.RefMan.vo.PatientProcedureForAppointmentOutcomeVoCollection getOutpatientProcedures()
	{
		return this.outpatientprocedures;
	}
	public void setOutpatientProcedures(ims.RefMan.vo.PatientProcedureForAppointmentOutcomeVoCollection value)
	{
		this.isValidated = false;
		this.outpatientprocedures = value;
	}
	public boolean getWardAttendanceHcpTypeIsNotNull()
	{
		return this.wardattendancehcptype != null;
	}
	public ims.core.vo.lookups.HcpDisType getWardAttendanceHcpType()
	{
		return this.wardattendancehcptype;
	}
	public void setWardAttendanceHcpType(ims.core.vo.lookups.HcpDisType value)
	{
		this.isValidated = false;
		this.wardattendancehcptype = value;
	}
	public boolean getWardAttendanceOutcomeIsNotNull()
	{
		return this.wardattendanceoutcome != null;
	}
	public ims.core.vo.lookups.WardAttendanceOutcome getWardAttendanceOutcome()
	{
		return this.wardattendanceoutcome;
	}
	public void setWardAttendanceOutcome(ims.core.vo.lookups.WardAttendanceOutcome value)
	{
		this.isValidated = false;
		this.wardattendanceoutcome = value;
	}
	public boolean getWardAttendanceOutcomeCommentIsNotNull()
	{
		return this.wardattendanceoutcomecomment != null;
	}
	public String getWardAttendanceOutcomeComment()
	{
		return this.wardattendanceoutcomecomment;
	}
	public static int getWardAttendanceOutcomeCommentMaxLength()
	{
		return 1500;
	}
	public void setWardAttendanceOutcomeComment(String value)
	{
		this.isValidated = false;
		this.wardattendanceoutcomecomment = value;
	}
	public boolean getApptStatusIsNotNull()
	{
		return this.apptstatus != null;
	}
	public ims.scheduling.vo.lookups.Status_Reason getApptStatus()
	{
		return this.apptstatus;
	}
	public void setApptStatus(ims.scheduling.vo.lookups.Status_Reason value)
	{
		this.isValidated = false;
		this.apptstatus = value;
	}
	public boolean getSessionIsNotNull()
	{
		return this.session != null;
	}
	public ims.RefMan.vo.SessionForAppointmentOutcomeVo getSession()
	{
		return this.session;
	}
	public void setSession(ims.RefMan.vo.SessionForAppointmentOutcomeVo value)
	{
		this.isValidated = false;
		this.session = value;
	}
	public boolean getAppointmentDateIsNotNull()
	{
		return this.appointmentdate != null;
	}
	public ims.framework.utils.Date getAppointmentDate()
	{
		return this.appointmentdate;
	}
	public void setAppointmentDate(ims.framework.utils.Date value)
	{
		this.isValidated = false;
		this.appointmentdate = value;
	}
	public boolean getSeenTimeIsNotNull()
	{
		return this.seentime != null;
	}
	public ims.framework.utils.Time getSeenTime()
	{
		return this.seentime;
	}
	public void setSeenTime(ims.framework.utils.Time value)
	{
		this.isValidated = false;
		this.seentime = value;
	}
	public boolean getApptStatusHistoryIsNotNull()
	{
		return this.apptstatushistory != null;
	}
	public ims.scheduling.vo.Appointment_StatusVoCollection getApptStatusHistory()
	{
		return this.apptstatushistory;
	}
	public void setApptStatusHistory(ims.scheduling.vo.Appointment_StatusVoCollection value)
	{
		this.isValidated = false;
		this.apptstatushistory = value;
	}
	public boolean getApptTrackingStatusHistoryIsNotNull()
	{
		return this.appttrackingstatushistory != null;
	}
	public ims.scheduling.vo.Appt_Tracking_Status_HistoryVoCollection getApptTrackingStatusHistory()
	{
		return this.appttrackingstatushistory;
	}
	public void setApptTrackingStatusHistory(ims.scheduling.vo.Appt_Tracking_Status_HistoryVoCollection value)
	{
		this.isValidated = false;
		this.appttrackingstatushistory = value;
	}
	public boolean getApptStartTimeIsNotNull()
	{
		return this.apptstarttime != null;
	}
	public ims.framework.utils.Time getApptStartTime()
	{
		return this.apptstarttime;
	}
	public void setApptStartTime(ims.framework.utils.Time value)
	{
		this.isValidated = false;
		this.apptstarttime = value;
	}
	public boolean getCurrentStatusRecordIsNotNull()
	{
		return this.currentstatusrecord != null;
	}
	public ims.scheduling.vo.Appointment_StatusVo getCurrentStatusRecord()
	{
		return this.currentstatusrecord;
	}
	public void setCurrentStatusRecord(ims.scheduling.vo.Appointment_StatusVo value)
	{
		this.isValidated = false;
		this.currentstatusrecord = value;
	}
	public boolean isValidated()
	{
		if(this.isBusy)
			return true;
		this.isBusy = true;
	
		if(!this.isValidated)
		{
			this.isBusy = false;
			return false;
		}
		if(this.outpatientprocedures != null)
		{
			if(!this.outpatientprocedures.isValidated())
			{
				this.isBusy = false;
				return false;
			}
		}
		if(this.apptstatushistory != null)
		{
			if(!this.apptstatushistory.isValidated())
			{
				this.isBusy = false;
				return false;
			}
		}
		if(this.appttrackingstatushistory != null)
		{
			if(!this.appttrackingstatushistory.isValidated())
			{
				this.isBusy = false;
				return false;
			}
		}
		if(this.currentstatusrecord != null)
		{
			if(!this.currentstatusrecord.isValidated())
			{
				this.isBusy = false;
				return false;
			}
		}
		this.isBusy = false;
		return true;
	}
	public String[] validate()
	{
		return validate(null);
	}
	public String[] validate(String[] existingErrors)
	{
		if(this.isBusy)
			return null;
		this.isBusy = true;
	
		java.util.ArrayList<String> listOfErrors = new java.util.ArrayList<String>();
		if(existingErrors != null)
		{
			for(int x = 0; x < existingErrors.length; x++)
			{
				listOfErrors.add(existingErrors[x]);
			}
		}
		if(this.outpatientprocedures != null)
		{
			String[] listOfOtherErrors = this.outpatientprocedures.validate();
			if(listOfOtherErrors != null)
			{
				for(int x = 0; x < listOfOtherErrors.length; x++)
				{
					listOfErrors.add(listOfOtherErrors[x]);
				}
			}
		}
		if(this.wardattendanceoutcomecomment != null)
			if(this.wardattendanceoutcomecomment.length() > 1500)
				listOfErrors.add("The length of the field [wardattendanceoutcomecomment] in the value object [ims.core.vo.WardAttenderOutcomeVo] is too big. It should be less or equal to 1500");
		if(this.apptstatushistory != null)
		{
			String[] listOfOtherErrors = this.apptstatushistory.validate();
			if(listOfOtherErrors != null)
			{
				for(int x = 0; x < listOfOtherErrors.length; x++)
				{
					listOfErrors.add(listOfOtherErrors[x]);
				}
			}
		}
		if(this.appttrackingstatushistory != null)
		{
			String[] listOfOtherErrors = this.appttrackingstatushistory.validate();
			if(listOfOtherErrors != null)
			{
				for(int x = 0; x < listOfOtherErrors.length; x++)
				{
					listOfErrors.add(listOfOtherErrors[x]);
				}
			}
		}
		if(this.currentstatusrecord != null)
		{
			String[] listOfOtherErrors = this.currentstatusrecord.validate();
			if(listOfOtherErrors != null)
			{
				for(int x = 0; x < listOfOtherErrors.length; x++)
				{
					listOfErrors.add(listOfOtherErrors[x]);
				}
			}
		}
		int errorCount = listOfErrors.size();
		if(errorCount == 0)
		{
			this.isBusy = false;
			this.isValidated = true;
			return null;
		}
		String[] result = new String[errorCount];
		for(int x = 0; x < errorCount; x++)
			result[x] = (String)listOfErrors.get(x);
		this.isBusy = false;
		this.isValidated = false;
		return result;
	}
	public void clearIDAndVersion()
	{
		this.id = null;
		this.version = 0;
	}
	public Object clone()
	{
		if(this.isBusy)
			return this;
		this.isBusy = true;
	
		WardAttenderOutcomeVo clone = new WardAttenderOutcomeVo(this.id, this.version);
		
		if(this.seenby == null)
			clone.seenby = null;
		else
			clone.seenby = (ims.core.vo.HcpLiteVo)this.seenby.clone();
		if(this.outpatientprocedures == null)
			clone.outpatientprocedures = null;
		else
			clone.outpatientprocedures = (ims.RefMan.vo.PatientProcedureForAppointmentOutcomeVoCollection)this.outpatientprocedures.clone();
		if(this.wardattendancehcptype == null)
			clone.wardattendancehcptype = null;
		else
			clone.wardattendancehcptype = (ims.core.vo.lookups.HcpDisType)this.wardattendancehcptype.clone();
		if(this.wardattendanceoutcome == null)
			clone.wardattendanceoutcome = null;
		else
			clone.wardattendanceoutcome = (ims.core.vo.lookups.WardAttendanceOutcome)this.wardattendanceoutcome.clone();
		clone.wardattendanceoutcomecomment = this.wardattendanceoutcomecomment;
		if(this.apptstatus == null)
			clone.apptstatus = null;
		else
			clone.apptstatus = (ims.scheduling.vo.lookups.Status_Reason)this.apptstatus.clone();
		if(this.session == null)
			clone.session = null;
		else
			clone.session = (ims.RefMan.vo.SessionForAppointmentOutcomeVo)this.session.clone();
		if(this.appointmentdate == null)
			clone.appointmentdate = null;
		else
			clone.appointmentdate = (ims.framework.utils.Date)this.appointmentdate.clone();
		if(this.seentime == null)
			clone.seentime = null;
		else
			clone.seentime = (ims.framework.utils.Time)this.seentime.clone();
		if(this.apptstatushistory == null)
			clone.apptstatushistory = null;
		else
			clone.apptstatushistory = (ims.scheduling.vo.Appointment_StatusVoCollection)this.apptstatushistory.clone();
		if(this.appttrackingstatushistory == null)
			clone.appttrackingstatushistory = null;
		else
			clone.appttrackingstatushistory = (ims.scheduling.vo.Appt_Tracking_Status_HistoryVoCollection)this.appttrackingstatushistory.clone();
		if(this.apptstarttime == null)
			clone.apptstarttime = null;
		else
			clone.apptstarttime = (ims.framework.utils.Time)this.apptstarttime.clone();
		if(this.currentstatusrecord == null)
			clone.currentstatusrecord = null;
		else
			clone.currentstatusrecord = (ims.scheduling.vo.Appointment_StatusVo)this.currentstatusrecord.clone();
		clone.isValidated = this.isValidated;
		
		this.isBusy = false;
		return clone;
	}
	public int compareTo(Object obj)
	{
		return compareTo(obj, true);
	}
	public int compareTo(Object obj, boolean caseInsensitive)
	{
		if (obj == null)
		{
			return -1;
		}
		if(caseInsensitive); // this is to avoid eclipse warning only.
		if (!(WardAttenderOutcomeVo.class.isAssignableFrom(obj.getClass())))
		{
			throw new ClassCastException("A WardAttenderOutcomeVo object cannot be compared an Object of type " + obj.getClass().getName());
		}
		if (this.id == null)
			return 1;
		if (((WardAttenderOutcomeVo)obj).getBoId() == null)
			return -1;
		return this.id.compareTo(((WardAttenderOutcomeVo)obj).getBoId());
	}
	public synchronized static int generateValueObjectUniqueID()
	{
		return ims.vo.ValueObject.generateUniqueID();
	}
	public int countFieldsWithValue()
	{
		int count = 0;
		if(this.seenby != null)
			count++;
		if(this.outpatientprocedures != null)
			count++;
		if(this.wardattendancehcptype != null)
			count++;
		if(this.wardattendanceoutcome != null)
			count++;
		if(this.wardattendanceoutcomecomment != null)
			count++;
		if(this.apptstatus != null)
			count++;
		if(this.session != null)
			count++;
		if(this.appointmentdate != null)
			count++;
		if(this.seentime != null)
			count++;
		if(this.apptstatushistory != null)
			count++;
		if(this.appttrackingstatushistory != null)
			count++;
		if(this.apptstarttime != null)
			count++;
		if(this.currentstatusrecord != null)
			count++;
		return count;
	}
	public int countValueObjectFields()
	{
		return 13;
	}
	protected ims.core.vo.HcpLiteVo seenby;
	protected ims.RefMan.vo.PatientProcedureForAppointmentOutcomeVoCollection outpatientprocedures;
	protected ims.core.vo.lookups.HcpDisType wardattendancehcptype;
	protected ims.core.vo.lookups.WardAttendanceOutcome wardattendanceoutcome;
	protected String wardattendanceoutcomecomment;
	protected ims.scheduling.vo.lookups.Status_Reason apptstatus;
	protected ims.RefMan.vo.SessionForAppointmentOutcomeVo session;
	protected ims.framework.utils.Date appointmentdate;
	protected ims.framework.utils.Time seentime;
	protected ims.scheduling.vo.Appointment_StatusVoCollection apptstatushistory;
	protected ims.scheduling.vo.Appt_Tracking_Status_HistoryVoCollection appttrackingstatushistory;
	protected ims.framework.utils.Time apptstarttime;
	protected ims.scheduling.vo.Appointment_StatusVo currentstatusrecord;
	private boolean isValidated = false;
	private boolean isBusy = false;
}