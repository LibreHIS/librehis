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
// This code was generated by Bogdan Tofei using IMS Development Environment (version 1.80 build 5427.27524)
// Copyright (C) 1995-2014 IMS MAXIMS. All rights reserved.

package ims.scheduling.domain.impl;

import ims.RefMan.vo.CatsReferralRefVo;
import ims.RefMan.vo.lookups.ReferralApptStatus;
import ims.core.admin.domain.objects.CareContext;
import ims.core.clinical.vo.ServiceRefVo;
import ims.core.patient.domain.objects.Patient;
import ims.core.patient.vo.PatientRefVo;
import ims.core.vo.CareContextShortVo;
import ims.core.vo.PatientShort;
import ims.core.vo.ServiceFunctionLiteVo;
import ims.core.vo.domain.CareContextShortVoAssembler;
import ims.core.vo.domain.PatientShortAssembler;
import ims.core.vo.domain.ServiceFunctionLiteVoAssembler;
import ims.core.vo.domain.ServiceLiteVoAssembler;
import ims.core.vo.lookups.ServiceCategory;
import ims.core.vo.lookups.ServiceFunction;
import ims.core.vo.lookups.TimeUnitsSecondsToMonths;
import ims.domain.DomainFactory;
import ims.domain.exceptions.StaleObjectException;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.utils.Date;
import ims.scheduling.domain.base.impl.BaseFutureAppointmentWorklistImpl;
import ims.scheduling.domain.objects.FutureAppointmentDetails;
import ims.scheduling.vo.Booking_AppointmentRefVo;
import ims.scheduling.vo.FutureAppointmentDetailsRefVo;
import ims.scheduling.vo.FutureAppointmentDetailsVo;
import ims.scheduling.vo.FutureAppointmentListVoCollection;
import ims.scheduling.vo.FutureAppointmentWorklistSearchCriteriaVo;
import ims.scheduling.vo.domain.BookAppointmentStartDateVoAssembler;
import ims.scheduling.vo.domain.FutureAppointmentDetailsVoAssembler;
import ims.scheduling.vo.domain.FutureAppointmentListVoAssembler;
import ims.scheduling.vo.lookups.FutureAppointmentSource;

import java.util.ArrayList;
import java.util.List;

public class FutureAppointmentWorklistImpl extends BaseFutureAppointmentWorklistImpl
{

	private static final long serialVersionUID = 1L;

	public ims.core.vo.ServiceLiteVoCollection getServices()
	{
		DomainFactory factory = getDomainFactory();
		
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();

		String hql = "from Service service where service.isActive = :isActive and service.canBeScheduled = :canBeScheduled and service.serviceCategory.id != :serviceCategoryID " ;
				
		markers.add("isActive");
		values.add(Boolean.TRUE);

		markers.add("canBeScheduled");
		values.add(Boolean.TRUE);
		
		markers.add("serviceCategoryID");
		values.add(ServiceCategory.RADIOLOGY_MODALITY.getID());
		
		return ServiceLiteVoAssembler.createServiceLiteVoCollectionFromService(factory.find(hql, markers, values));
	}

	public ims.core.vo.ServiceFunctionLiteVoCollection getFunctionsForService(ServiceRefVo service)
	{
		if (service == null || service.getID_Service() == null)
			return null; 

		/* TODO MSSQL case - List list = getDomainFactory().find("select servFunct from ServiceFunction as servFunct left join servFunct.service as serv where servFunct.isActive = 1 and serv.id = :serviceID",
				new String[] {"serviceID"}, new Object[] {service.getID_Service()}); */
		List list = getDomainFactory().find("select servFunct from ServiceFunction as servFunct left join servFunct.service as serv where servFunct.isActive = true and serv.id = :serviceID",
				new String[] {"serviceID"}, new Object[] {service.getID_Service()});
		
		if (list != null && list.size() > 0)
			return ServiceFunctionLiteVoAssembler.createServiceFunctionLiteVoCollectionFromServiceFunction(list);
		
		return null;
	}

	public FutureAppointmentListVoCollection getFutureAppointments(FutureAppointmentWorklistSearchCriteriaVo searchCriteria)
	{
		DomainFactory factory = getDomainFactory();

		String hql = " select futureApp from FutureAppointmentDetails as futureApp left join futureApp.source as source left join futureApp.referral as cats left join cats.currentStatus as catsStatus left join catsStatus.referralStatus as referralStatus ";

		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();
		String andStr = " where ";
		StringBuffer condStr = new StringBuffer();

		if (searchCriteria != null)
		{

			 if (searchCriteria.getService() != null)
			{
				hql += " left join futureApp._1stApptService as serv ";

				condStr.append(andStr + " serv.id = :serviceID ");
				markers.add("serviceID");
				values.add(searchCriteria.getService().getID_Service());
				andStr = " and ";

			}
			 if (searchCriteria.getNamedConsultantIsNotNull())//WDEV-21379
				{
					hql += " left join futureApp._1stApptToSee as consToSee left join consToSee.namedConsultantValue as namedCons ";

					condStr.append(andStr + " namedCons.id = :NamedConsultantID ");
					markers.add("NamedConsultantID");
					values.add(searchCriteria.getNamedConsultant().getID_Hcp());
					andStr = " and ";

				}

			if (searchCriteria.getFunction() != null && searchCriteria.getFunction().getFunction() != null)
			{
				hql += " left join futureApp._1stApptFunction as func ";

				condStr.append(andStr + " func.id = :functionID ");
				markers.add("functionID");
				values.add(searchCriteria.getFunction().getFunction().getID());
				andStr = " and ";

			}
			
			boolean isSearchByDate = searchCriteria.getStartDate() != null || searchCriteria.getEndDate() != null;//WDEV-22360
			
			if (isSearchByDate)
				andStr = " and (";
			
			if (searchCriteria.getStartDate() != null)
			{
				condStr.append(andStr + " futureApp._1stApptFutureApptByDate >= :startDate  ");
				markers.add("startDate");
				values.add(searchCriteria.getStartDate().getDate());
				andStr = " and ";
				
			}

			if (searchCriteria.getEndDate() != null)
			{
				condStr.append(andStr + " futureApp._1stApptFutureApptByDate <= :endDate ");
				markers.add("endDate");
				values.add(searchCriteria.getEndDate().getDate());
				andStr = " and ";
				
			}
			
			if(isSearchByDate)//WDEV-22360
			{
				andStr = "or (";

				if (searchCriteria.getStartDate() != null)
				{
					condStr.append(andStr + " futureApp.startingWeek >= :startDate ");
					markers.add("startDate");
					values.add(searchCriteria.getStartDate().getDate());
					andStr = " and ";
				}

				if (searchCriteria.getEndDate() != null)
				{
					condStr.append(andStr + " futureApp.startingWeek <= :endDate ");
					markers.add("endDate");
					values.add(searchCriteria.getEndDate().getDate());
					andStr = " and ";	
				}
				condStr.append(" ))");
			}	
			if (searchCriteria.getStatus() != null)
			{
				hql += " left join futureApp.currentStatus as status left join status.pendingStatus as pendingStat";
				
				condStr.append(andStr + " pendingStat.id = :statusID ");
				markers.add("statusID");
				values.add(searchCriteria.getStatus().getID());
				andStr = " and ";
				
			}
			
			if (searchCriteria.getDelayedBy() != null && searchCriteria.getDelayedByType() != null)
			{
				Date date = calculatePastAppointmentDate(searchCriteria.getDelayedBy(), searchCriteria.getDelayedByType());
				
				condStr.append(andStr + " futureApp._1stApptFutureApptByDate <= :DATE ");
				markers.add("DATE");
				values.add(date.getDate());
				andStr = " and ";
				
			}
			else if (searchCriteria.getTimeOnList() != null && searchCriteria.getTimeOnlistType() != null)
			{
				Date date = calculateFutureAppointmentDate(searchCriteria.getTimeOnList(), searchCriteria.getTimeOnlistType());
				
				condStr.append(andStr + " futureApp._1stApptFutureApptByDate >= :DATE ");
				markers.add("DATE");
				values.add(date.getDate());
				andStr = " and ";
			}
			
			if (Boolean.TRUE.equals(searchCriteria.getConsultant()) || Boolean.TRUE.equals(searchCriteria.getAssociateSpecialist()) 
				|| Boolean.TRUE.equals(searchCriteria.getSPR()) || Boolean.TRUE.equals(searchCriteria.getAnyDoctorToSee() || searchCriteria.getHCP() != null))
			{
				hql += " left join futureApp._1stApptToSee as toSee ";
				
				boolean atLeastOneAdded = false;
				
				if (Boolean.TRUE.equals(searchCriteria.getConsultant()))
				{
					atLeastOneAdded = true;

					/* TODO MSSQL case -  condStr.append(andStr + " ( toSee.consultant = 1 "); */
				    condStr.append(andStr + " ( toSee.consultant = true ");
				}
				
				if (Boolean.TRUE.equals(searchCriteria.getAssociateSpecialist()))
				{
					if (!atLeastOneAdded)
					{
						/* TODO MSSQL case - condStr.append(andStr + " ( toSee.associateSpecialist = 1 "); */
						condStr.append(andStr + " ( toSee.associateSpecialist = true ");
						atLeastOneAdded = true;
					}
					else
					{
						/* TODO MSSQL case - condStr.append(" or toSee.associateSpecialist = 1 "); */
						condStr.append(" or toSee.associateSpecialist = true ");
					}
				}
				
				if (Boolean.TRUE.equals(searchCriteria.getSPR()))
				{
					if (!atLeastOneAdded)
					{
						/* TODO MSSQL case - condStr.append(andStr + " ( toSee.sPR = 1 "); */
						condStr.append(andStr + " ( toSee.sPR = true ");
						atLeastOneAdded = true;
					}
					else
					{
						/* TODO MSSQL case - condStr.append(" or toSee.sPR = 1 "); */
						condStr.append(" or toSee.sPR = true ");
					}
				}
				
				if (Boolean.TRUE.equals(searchCriteria.getAnyDoctorToSee()))
				{
					if (!atLeastOneAdded)
					{
						/* TODO MSSQL case - condStr.append(andStr + " ( toSee.anyDoctorToSee = 1 "); */
						condStr.append(andStr + " ( toSee.anyDoctorToSee = true ");
						atLeastOneAdded = true;
					}
					else
					{
						/* TODO MSSQL case - condStr.append(" or toSee.anyDoctorToSee = 1 "); */
						condStr.append(" or toSee.anyDoctorToSee = true ");
					}
				}
				
				if (searchCriteria.getHCP() != null)
				{
					hql += " left join toSee.otherHCPValue as hcp ";
					
					if (!atLeastOneAdded)
					{
						condStr.append(andStr + " ( hcp.id = :hcpID ");
						atLeastOneAdded = true;
					}
					else
					{
						condStr.append(" or hcp.id = :hcpID ");
					}
					
					markers.add("hcpID");
					values.add(searchCriteria.getHCP().getID());
				}
				
				andStr = " and ";
				condStr.append(" ) ");
			}
			
		}

		/* TODO MSSQL case - condStr.append(andStr + " ( futureApp.isRIE = 0 or futureApp.isRIE is null) and source.id = :sourceID and referralStatus.id <> :EndOfTreatmentId "); */
		condStr.append(andStr + " ( futureApp.isRIE = FALSE or futureApp.isRIE is null) and source.id = :sourceID and referralStatus.id <> :EndOfTreatmentId ");
		
		markers.add("sourceID");
		values.add(FutureAppointmentSource.APPOINTMENT_OUTCOME.getID());
		
		markers.add("EndOfTreatmentId");
		values.add(ReferralApptStatus.END_OF_CARE.getID());

		hql += condStr.toString();

		return FutureAppointmentListVoAssembler.createFutureAppointmentListVoCollectionFromFutureAppointmentDetails(factory.find(hql, markers, values));

	}

	public FutureAppointmentDetailsVo getFutureAppointmentDetails(FutureAppointmentDetailsRefVo futureAppt)
	{
		if (futureAppt == null || futureAppt.getID_FutureAppointmentDetails() == null)
			return null;
		
		return FutureAppointmentDetailsVoAssembler.create((FutureAppointmentDetails)getDomainFactory().getDomainObject(FutureAppointmentDetails.class, futureAppt.getID_FutureAppointmentDetails()));
		
	}

	public PatientShort getPatientShort(PatientRefVo patient)
	{
		if (patient == null || patient.getID_Patient() == null)
			return null;
		
		return PatientShortAssembler.create((Patient)getDomainFactory().getDomainObject(Patient.class, patient.getID_Patient()));
	}

	public void saveFutureAppointment(FutureAppointmentDetailsVo futureAppointment) throws StaleObjectException
	{
		if (futureAppointment == null)
			throw new CodingRuntimeException("Cannot save null Future Appointment");
		
		DomainFactory factory = getDomainFactory();
		
		FutureAppointmentDetails domAppt = FutureAppointmentDetailsVoAssembler.extractFutureAppointmentDetails(factory, futureAppointment);
		
		factory.save(domAppt);
	}
	
	private Date calculatePastAppointmentDate(Integer value , TimeUnitsSecondsToMonths unit)
	{
		if(value == null || unit == null)
			return null;
		
		Date pastDate = new Date();

		if (unit.equals(TimeUnitsSecondsToMonths.DAYS))
			pastDate.addDay(- value);
		if (unit.equals(TimeUnitsSecondsToMonths.WEEKS))
			pastDate.addDay(- (value * 7));
		if (unit.equals(TimeUnitsSecondsToMonths.MONTHS))
			pastDate.addMonth(- value);
		
		return pastDate;
	}
	
	private Date calculateFutureAppointmentDate(Integer value , TimeUnitsSecondsToMonths unit)
	{
		if(value == null || unit == null)
			return null;
		
		Date futureDate = new Date();

		if (unit.equals(TimeUnitsSecondsToMonths.DAYS))
			futureDate.addDay(value);
		if (unit.equals(TimeUnitsSecondsToMonths.WEEKS))
			futureDate.addDay(value * 7);
		if (unit.equals(TimeUnitsSecondsToMonths.MONTHS))
			futureDate.addMonth(value);
		
		return futureDate;
	}

	public ServiceFunctionLiteVo getServiceFunction(ServiceRefVo service, ServiceFunction function)
	{
		if (service == null || service.getID_Service() == null || function == null)
			return null;

		/* TODO MSSQL case - List list = getDomainFactory().find("select servFunct from ServiceFunction as servFunct left join servFunct.service as serv left join servFunct.function as func where servFunct.isActive = 1 and serv.id = :serviceID and func.id = :functionID",
				new String[] {"serviceID", "functionID"}, new Object[] {service.getID_Service(), function.getID()}); */
		List list = getDomainFactory().find("select servFunct from ServiceFunction as servFunct left join servFunct.service as serv left join servFunct.function as func where servFunct.isActive = true and serv.id = :serviceID and func.id = :functionID",
				new String[] {"serviceID", "functionID"}, new Object[] {service.getID_Service(), function.getID()});
		
		if (list != null && list.size() > 0)
			return ServiceFunctionLiteVoAssembler.createServiceFunctionLiteVoCollectionFromServiceFunction(list).get(0);
		
		return null;
	}

	public Booking_AppointmentRefVo getAppointmentForRecord(FutureAppointmentDetailsRefVo futureAppointment)
	{
		if (futureAppointment == null || futureAppointment.getID_FutureAppointmentDetails() == null)
			return null;
		
		List list = getDomainFactory().find("select appt from Booking_Appointment as appt left join appt.outcomeActions as actions left join actions.futureAppointmentDetail as futureAppt where futureAppt.id = :futureAppointmentID", 
				new String[] {"futureAppointmentID"}, new Object[] {futureAppointment.getID_FutureAppointmentDetails()});
		
		if (list != null && list.size() > 0)
			return BookAppointmentStartDateVoAssembler.createBookAppointmentStartDateVoCollectionFromBooking_Appointment(list).get(0);
		
		return null;
	}

	public CareContextShortVo getCareContextForReferral(CatsReferralRefVo referal)
	{
		if (referal == null || referal.getID_CatsReferral() == null)
			return null; 
		
		List list = getDomainFactory().find("select cc from CatsReferral as referral left join referral.careContext as cc where referral.id = :referralID", 
				new String[] {"referralID"}, new Object[] {referal.getID_CatsReferral()});
		
		if (list != null && list.size() > 0)
			return CareContextShortVoAssembler.create((CareContext) list.get(0));
		
		return null;
	}
}
