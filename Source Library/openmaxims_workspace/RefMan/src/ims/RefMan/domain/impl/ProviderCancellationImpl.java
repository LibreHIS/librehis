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
// This code was generated by Catalin Tomozei using IMS Development Environment (version 1.65 build 3223.30681)
// Copyright (C) 1995-2008 IMS MAXIMS plc. All rights reserved.

package ims.RefMan.domain.impl;

import ims.RefMan.domain.base.impl.BaseProviderCancellationImpl;
import ims.RefMan.domain.objects.CatsReferral;
import ims.RefMan.domain.objects.ElectiveListStatus;
import ims.RefMan.domain.objects.PatientElectiveList;
import ims.RefMan.domain.objects.ReferralEROD;
import ims.RefMan.domain.objects.TCIForPatientElectiveList;
import ims.RefMan.domain.objects.TCIOutcomeForPatientElectiveList;
import ims.RefMan.vo.CatsReferralProviderCancellationVo;
import ims.RefMan.vo.CatsReferralRefVo;
import ims.RefMan.vo.domain.CatsReferralProviderCancellationVoAssembler;
import ims.RefMan.vo.lookups.AdmissionOfferOutcome;
import ims.RefMan.vo.lookups.ERODStatus;
import ims.RefMan.vo.lookups.TCIStatusChangeReason;
import ims.chooseandbook.vo.lookups.ActionRequestType;
import ims.configuration.gen.ConfigFlag;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.people.domain.objects.MemberOfStaff;
import ims.core.vo.MemberOfStaffLiteVo;
import ims.core.vo.MemberOfStaffShortVo;
import ims.core.vo.ReferralNoteVo;
import ims.core.vo.domain.MemberOfStaffLiteVoAssembler;
import ims.core.vo.domain.MemberOfStaffShortVoAssembler;
import ims.core.vo.domain.ReferralNoteVoAssembler;
import ims.core.vo.lookups.WaitingListStatus;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.lookups.LookupInstance;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.pathways.configuration.domain.objects.RTTStatusPoint;
import ims.pathways.domain.AdminEvent;
import ims.pathways.domain.HL7PathwayIf;
import ims.pathways.domain.impl.AdminEventImpl;
import ims.pathways.domain.impl.HL7PathwayIfImpl;
import ims.pathways.domain.objects.PathwayClock;
import ims.pathways.domain.objects.PathwayRTTStatus;
import ims.pathways.domain.objects.PathwaysRTTClockImpact;
import ims.pathways.domain.objects.PatientJourneyStatus;
import ims.pathways.domain.objects.PatientPathwayJourney;
import ims.pathways.domain.objects.RTTStatusEventMap;
import ims.pathways.vo.PatientEventVo;
import ims.pathways.vo.RTTStatusEventMapRefVo;
import ims.pathways.vo.RTTStatusEventMapVo;
import ims.pathways.vo.domain.PatientJourneyVoAssembler;
import ims.pathways.vo.domain.RTTStatusEventMapVoAssembler;
import ims.pathways.vo.lookups.EventStatus;
import ims.pathways.vo.lookups.JourneyStatus;
import ims.pathways.vo.lookups.RTTClockImpactSource;
import ims.pathways.vo.lookups.RTTClockState;
import ims.scheduling.domain.SessionAdmin;
import ims.scheduling.domain.impl.SessionAdminImpl;
import ims.scheduling.domain.objects.Appointment_Status;
import ims.scheduling.domain.objects.Booking_Appointment;
import ims.scheduling.vo.Booking_AppointmentVo;
import ims.scheduling.vo.domain.Booking_AppointmentVoAssembler;
import ims.scheduling.vo.lookups.Status_Reason;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ProviderCancellationImpl extends BaseProviderCancellationImpl
{
	private static final long serialVersionUID = 1L;
	private static final int CATSREFERRAL_CANCELLED_BY_PROVIDER_NAT_CODE = 34;

	// WDEV-12088 - referralNote argument added
	// WDEV-18087 - patElectiveListCancellation argument added
	public CatsReferralProviderCancellationVo saveReferral(CatsReferralProviderCancellationVo vo, ReferralNoteVo referralNote) throws StaleObjectException, DomainInterfaceException
	{
		if (vo == null)
			throw new DomainRuntimeException("CatsReferralProviderCancellationVo is null");
		if (vo != null && !vo.isValidated())
			throw new DomainRuntimeException("CatsReferralProviderCancellationVo is not validated");

		// WDEV-12088 - starts here
		if (referralNote == null)
			throw new CodingRuntimeException("Can not save a null ReferralNote.");
		if (!referralNote.isValidated())
			throw new CodingRuntimeException("ReferralNote is not validated.");
		// WDEV-12088 - ends here

		DomainFactory factory = getDomainFactory();

		SessionAdmin sessionAdminImpl = (SessionAdmin) getDomainImpl(SessionAdminImpl.class);

		// WDEV-18087
		PatientElectiveList doPatientElectiveList = null;

		String patientElectiveListQuery = "select electiveList from PatientElectiveList as electiveList left join electiveList.referral as cats left join electiveList.pathwayClock as pc where cats.id = :CatsId ";
		List<?> electiveList = factory.find(patientElectiveListQuery, new String[] { "CatsId" }, new Object[] { vo.getID_CatsReferral() });

		if (electiveList != null && electiveList.size() > 0)
		{
			for (int i = 0; i < electiveList.size(); i++)
			{
				doPatientElectiveList = (PatientElectiveList) electiveList.get(i);

				if (doPatientElectiveList != null && !getDomLookup(WaitingListStatus.REMOVED).equals(doPatientElectiveList.getElectiveListStatus().getElectiveListStatus()))
				{
					MemberOfStaff domainMOS = null;
					Object mosUser = getMosUser();

					if (mosUser instanceof MemberOfStaffShortVo)
					{
						domainMOS = MemberOfStaffShortVoAssembler.extractMemberOfStaff(factory, ((MemberOfStaffShortVo) getMosUser()));
					}

					ElectiveListStatus status = new ElectiveListStatus();
					status.setElectiveListStatus(getDomLookup(WaitingListStatus.REMOVED));
					status.setAuthoringUser(domainMOS);
					status.setStatusDateTime(new java.util.Date());

					doPatientElectiveList.setElectiveListStatus(status);

					if (doPatientElectiveList.getElectiveListStatusHistory() == null)
						doPatientElectiveList.setElectiveListStatusHistory(new ArrayList());

					doPatientElectiveList.getElectiveListStatusHistory().add(status);

					TCIForPatientElectiveList doTCIDetails = doPatientElectiveList.getTCIDetails();

					if (doTCIDetails != null && doTCIDetails.isIsActive())
					{
						TCIOutcomeForPatientElectiveList newOutcome = new TCIOutcomeForPatientElectiveList();

						if (doTCIDetails.getTCIDate() != null && (new java.util.Date()).before(doTCIDetails.getTCIDate()))
							newOutcome.setOutcome(getDomLookup(AdmissionOfferOutcome.ADMISSION_CANCELLED_BY_HOSPITAL_BEFORE_6));
						else
							newOutcome.setOutcome(getDomLookup(AdmissionOfferOutcome.ADMISSION_CANCELLED_BY_HOSPITAL_ON_DAY_7));

						newOutcome.setChangeBy(domainMOS);
						newOutcome.setStatusDateTime(new java.util.Date());
						newOutcome.setOutcomeReason(getDomLookup(TCIStatusChangeReason.CANCELLEDBYREMOVALOFELECTIVELISTRECORD));

						doTCIDetails.setCurrentOutcome(newOutcome);
						if (doTCIDetails.getOutcomeHistory() == null)
							doTCIDetails.setOutcomeHistory(new ArrayList());

						doTCIDetails.setIsActive(false);
						doTCIDetails.getOutcomeHistory().add(newOutcome);

					}

					ReferralEROD doERODDetails = doPatientElectiveList.getEROD();

					if (doERODDetails != null)
					{
						doERODDetails.setIsActive(false);
						doERODDetails.setErodStatus(getDomLookup(ERODStatus.CANCELLED));
					}

					factory.save(doPatientElectiveList);
				}
			}
		}
		// End WDEV-18087

		CatsReferral doCats = CatsReferralProviderCancellationVoAssembler.extractCatsReferral(factory, vo);

		// WDEV-18328
		// update Appointments from CatsReferral

		Date currentDate = new Date();

		if (doCats.getAppointments() != null)
		{
			Iterator it = doCats.getAppointments().iterator();

			while (it.hasNext())
			{
				Booking_Appointment doBookAppt = (Booking_Appointment) it.next();

				if (doBookAppt != null && getDomLookup(Status_Reason.BOOKED).equals(doBookAppt.getApptStatus()) && currentDate.getDate().compareTo(doBookAppt.getAppointmentDate()) <= 0)
				{
					doBookAppt.setApptStatus(getDomLookup(Status_Reason.CANCELLED));

					if (doBookAppt.getCurrentStatusRecord() != null)
					{
						Appointment_Status newStatus = new Appointment_Status();

						// populate old status fields to this new status
						populateOldFields(doBookAppt, newStatus);

						// populate the new values
						newStatus.setStatus(getDomLookup(Status_Reason.CANCELLED));
						newStatus.setStatusChangeDateTime(new java.util.Date());

						// add this to Appointment Status History
						if (doBookAppt.getApptStatusHistory() == null)
							doBookAppt.setApptStatusHistory(new HashSet());

						doBookAppt.setCurrentStatusRecord(newStatus);
						doBookAppt.getApptStatusHistory().add(newStatus);
					}

					Booking_AppointmentVo bookingApptVo = Booking_AppointmentVoAssembler.create(doBookAppt);
					if (bookingApptVo.getSessionSlot() != null)
					{
						bookingApptVo.getSessionSlot().setStatus(bookingApptVo.getSession().getAppropiateSessionSlotStatus()); // WDEV-18940
					}

					sessionAdminImpl.cancelAppt(bookingApptVo, ActionRequestType.NOTIFY_APPT_CANCEL, "Cancel Appt requested from Provider Cancellation");
				}
			}
		}

		// update ConsultationAppt from CatsReferral
		if (doCats.getConsultationAppt() != null && getDomLookup(Status_Reason.BOOKED).equals(doCats.getConsultationAppt().getApptStatus()) && currentDate.getDate().compareTo(doCats.getConsultationAppt().getAppointmentDate()) <= 0)
		{

			Booking_Appointment appt = doCats.getConsultationAppt();

			appt.setApptStatus(getDomLookup(Status_Reason.CANCELLED));
			appt.setApptStatusReas(getDomLookup(Status_Reason.HOSPITALCANCELLED));

			if (appt.getCurrentStatusRecord() != null)
			{
				Appointment_Status newStatus = new Appointment_Status();

				newStatus = appt.getCurrentStatusRecord();

				// populate old status fields to this new status
				populateOldFields(appt, newStatus);

				newStatus.setStatus(getDomLookup(Status_Reason.CANCELLED));
				newStatus.setStatusChangeDateTime(new java.util.Date());

				// add this to Appointment Status History
				if (appt.getApptStatusHistory() == null)
					appt.setApptStatusHistory(new HashSet());

				appt.setCurrentStatusRecord(newStatus);
				appt.getApptStatusHistory().add(newStatus);
			}

			Booking_AppointmentVo bookingApptVo = Booking_AppointmentVoAssembler.create(appt); // WDEV-18940
			if (bookingApptVo.getSessionSlot() != null)
			{
				bookingApptVo.getSessionSlot().setStatus(bookingApptVo.getSession().getAppropiateSessionSlotStatus());
			}

			sessionAdminImpl.cancelAppt(bookingApptVo, ActionRequestType.NOTIFY_APPT_CANCEL, "Cancel Appt requested from Provider Cancellation");
		}

		// update the patient journey
		PatientPathwayJourney doJourney = doCats.getJourney();

		// Place holders for clock Impact variables
		PathwaysRTTClockImpact clockImpact = null;
		PathwayClock initialClock = null;
		PathwayRTTStatus initialRTTStatus = null;
		LookupInstance initialClockState = null;
		boolean wasClockStarted = false; // since we can't create a copy of the initial clock and it's possible it will be changed
		boolean wasClockStopped = false; // retain in these 2 boolean variables the state of the initial clock

		if (doJourney != null)
		{
			initialClock = doJourney.getCurrentClock();
			wasClockStarted = initialClock != null && initialClock.getStartDate() != null;
			wasClockStopped = initialClock != null && initialClock.getStopDate() != null;
			initialClockState = getClockState(initialClock);
			initialRTTStatus = doCats.getCurrentRTTStatus();

			PatientJourneyStatus journeyStatus = new PatientJourneyStatus();
			journeyStatus.setDateTime(new java.util.Date());
			journeyStatus.setStatus(getDomLookup(JourneyStatus.ENDPATHWAYJOURNEY));
			factory.save(journeyStatus);

			if (doJourney.getStatusHistory() == null)
			{
				doJourney.setStatusHistory(new HashSet());
			}

			if (doJourney.getCurrentClock() != null)
			{
				doJourney.getCurrentClock().setStopDate(new java.util.Date());
			}

			doJourney.setEndedOnDate(new java.util.Date());
			doJourney.setCurrentStatus(journeyStatus);
			doJourney.getStatusHistory().add(journeyStatus);

		}

		// update Elective EROD
		if (doCats.getElectiveEROD() != null)
		{
			for (int i = 0; i < doCats.getElectiveEROD().size(); i++)
			{
				ReferralEROD electiveErod = (ReferralEROD) doCats.getElectiveEROD().get(i);

				if (electiveErod != null && !getDomLookup(ERODStatus.CANCELLED).equals(electiveErod.getErodStatus()))
				{
					electiveErod.setIsActive(false);
					electiveErod.setErodStatus(getDomLookup(ERODStatus.CANCELLED));
				}
			}
		}

		// update Outpatient EROD
		if (doCats.getOutpatientEROD() != null)
		{
			for (int i = 0; i < doCats.getOutpatientEROD().size(); i++)
			{
				ReferralEROD outpatientErod = (ReferralEROD) doCats.getOutpatientEROD().get(i);

				if (outpatientErod != null && !getDomLookup(ERODStatus.CANCELLED).equals(outpatientErod.getErodStatus()))
				{
					outpatientErod.setIsActive(false);
					outpatientErod.setErodStatus(getDomLookup(ERODStatus.CANCELLED));
				}
			}
		}

		// WDEV-23646 - Ensure the correct event Date Time is used when creating a new RTT Status
		// Provider, patient or any other form of cancellation - record the actual time the cancellation occured
		java.util.Date eventDateTime = new java.util.Date();
		RTTStatusPoint rttStatusPoint = updateRTTStatus(doCats, eventDateTime);

		factory.save(doCats);

		if (ConfigFlag.DOM.RTT_STATUS_POINT_FUNCTIONALITY.getValue() && Boolean.TRUE.equals(doCats.isRTTClockImpact()) && rttStatusPoint != null && doCats.getJourney() != null)
		{
			RTTStatusEventMapVo rttMap = getRTTStatusEventMap(rttStatusPoint);

			if (rttMap != null && rttMap.getEvent() != null)
			{
				PatientEventVo patEvent = new PatientEventVo();
				patEvent.setPatient(new PatientRefVo(doCats.getPatient().getId(), doCats.getPatient().getVersion()));
				patEvent.setEvent(rttMap.getEvent());
				patEvent.setEventDateTime(new DateTime());
				patEvent.setEventStatus(EventStatus.ACTIVE);
				patEvent.setJourney(PatientJourneyVoAssembler.create(doCats.getJourney()));

				HL7PathwayIf impl = (HL7PathwayIf) getDomainImpl(HL7PathwayIfImpl.class);
				impl.instantiatePatientEvent(patEvent);
			}

			// Analyze clock impact here
			clockImpact = createRTTClockImpactRecord(initialClock, doCats.getJourney().getCurrentClock(), initialRTTStatus, doCats.getCurrentRTTStatus(), null, doCats.getJourney(), RTTClockImpactSource.REFERRAL_PROVIDER_CANCELLATION, wasClockStarted, wasClockStopped, initialClockState, getClockState(doCats.getJourney().getCurrentClock()));
		}
		
		if (clockImpact != null)
		{
			factory.save(clockImpact);

			if (doCats.getRTTClockImpacts() == null)
			{
				doCats.setRTTClockImpacts(new ArrayList());
			}
			doCats.getProviderCancellation().setRTTClockImpact(clockImpact);
			doCats.getRTTClockImpacts().add(clockImpact);
		}
		
		factory.save(doCats);

		// WDEV-12088 - starts here
		ims.core.domain.objects.CorrespondenceNote domainObject = ReferralNoteVoAssembler.extractCorrespondenceNote(factory, referralNote);
		factory.save(domainObject);
		// WDEV-12088 - ends here

		return CatsReferralProviderCancellationVoAssembler.create(doCats);
	}
	
	
	private LookupInstance getClockState(PathwayClock clock)
	{
		if (clock == null)
			return getDomLookup(RTTClockState.NOT_PRESENT);
		
		if (clock.getStopDate() != null)
			return getDomLookup(RTTClockState.STOPPED);
		
		return getDomLookup(RTTClockState.STARTED);
	}


	public PathwaysRTTClockImpact createRTTClockImpactRecord(PathwayClock initialClock, PathwayClock pathwayClock, PathwayRTTStatus initialRTTStatus, PathwayRTTStatus pathwayRTTStatus, RTTStatusEventMapRefVo eventMapRefVo, PatientPathwayJourney patientPathwayJourney, RTTClockImpactSource source, boolean wasClockStarted, boolean wasClockStopped, LookupInstance initialClockState, LookupInstance finalClockState)
	{
		PathwaysRTTClockImpact clockImpact = new PathwaysRTTClockImpact();
		clockImpact.setInitialClock(initialClock);
		clockImpact.setFinalClock(pathwayClock);
		clockImpact.setInitialClockState(initialClockState);
		clockImpact.setFinalClockState(finalClockState);
		clockImpact.setInitialRTTStatus(initialRTTStatus);
		clockImpact.setFinalRTTStatus(pathwayRTTStatus);

		if (eventMapRefVo != null && eventMapRefVo.getID_RTTStatusEventMap() != null)
		{
			clockImpact.setOutcomeEvent((RTTStatusEventMap) getDomainFactory().getDomainObject(RTTStatusEventMap.class, eventMapRefVo.getID_RTTStatusEventMap()));
		}
		else
		{
			clockImpact.setOutcomeEvent(null);
		}

		clockImpact.setJourney(patientPathwayJourney);
		clockImpact.setSource(getDomLookup(source));

		clockImpact.setClockStarted(Boolean.FALSE);
		clockImpact.setClockStopped(Boolean.FALSE);

		// Case 1 - If there was no clock initially and one clock was created
		if (initialClock == null && pathwayClock != null)
		{
			// New clock has a start date - mark the ClockImpact
			if (pathwayClock.getStartDate() != null)
				clockImpact.setClockStarted(Boolean.TRUE);

			if (pathwayClock.getStopDate() != null)
				clockImpact.setClockStopped(Boolean.TRUE);
		}

		// Case 2 - If there was a clock initially and there is no clock now
		if (initialClock != null && pathwayClock == null)
		{
			if (wasClockStopped == false)
				clockImpact.setClockStopped(Boolean.TRUE);
		}

		// Case 3 - If there was an initial clock and a clock is present now
		if (initialClock != null && pathwayClock != null)
		{
			// Case 3.1 - Initial and current clock are the same one
			if (initialClock.getId() == pathwayClock.getId())
			{
				if (!wasClockStopped && pathwayClock.getStopDate() != null)
					clockImpact.setClockStopped(Boolean.TRUE);

				if (wasClockStopped && pathwayClock.getStopDate() == null)
					clockImpact.setClockStarted(Boolean.TRUE);

				if (!wasClockStarted && pathwayClock.getStartDate() != null)
					clockImpact.setClockStarted(Boolean.TRUE);
			}

			// Case 3.2 - Initial and current clock are not the same one
			if (initialClock.getId() != pathwayClock.getId())
			{
				if (!wasClockStopped)
					clockImpact.setClockStopped(Boolean.TRUE);

				if (pathwayClock.getStartDate() != null)
					clockImpact.setClockStarted(Boolean.TRUE);

				if (pathwayClock.getStopDate() != null)
					clockImpact.setClockStopped(Boolean.TRUE);
			}
		}

		return clockImpact;
	}

	// WDEV-23646 - Ensure the correct event Date Time is used when creating a new RTT Status
	private RTTStatusPoint updateRTTStatus(CatsReferral doCats, java.util.Date eventDateTime) throws DomainInterfaceException, StaleObjectException
	{
		if (!ConfigFlag.DOM.RTT_STATUS_POINT_FUNCTIONALITY.getValue())
			return null;

		if (doCats == null)
			return null;

		if (doCats.isRTTClockImpact() == null || Boolean.FALSE.equals(doCats.isRTTClockImpact()))
			return null;

		PathwayRTTStatus rttStatus = createPathwayRTTStatus(doCats, eventDateTime);

		doCats.setCurrentRTTStatus(rttStatus);

		return rttStatus.getRTTStatus();
	}

	private RTTStatusEventMapVo getRTTStatusEventMap(RTTStatusPoint rttStatusPoint)
	{
		if (rttStatusPoint == null)
			return null;

		/* TODO MSSQL case - String query = "select rttMap from RTTStatusEventMap as rttMap left join rttMap.currentRTTStatus as rtt where rtt.id = :RTTStatusPoint and rttMap.event is not null and rttMap.active = 1 and rttMap.encounterType is null"; */
		String query = "select rttMap from RTTStatusEventMap as rttMap left join rttMap.currentRTTStatus as rtt where rtt.id = :RTTStatusPoint and rttMap.event is not null and rttMap.active = true and rttMap.encounterType is null";

		List<?> listRTTMap = getDomainFactory().find(query, new String[] { "RTTStatusPoint" }, new Object[] { rttStatusPoint.getId() });

		if (listRTTMap != null && listRTTMap.size() > 0 && listRTTMap.get(0) instanceof RTTStatusEventMap)
		{
			return RTTStatusEventMapVoAssembler.create((RTTStatusEventMap) listRTTMap.get(0));
		}

		return null;
	}

	// Ensure the correct event Date Time is used when creating a new RTT Status
	private PathwayRTTStatus createPathwayRTTStatus(CatsReferral doCats, java.util.Date eventDateTime)
	{
		if (doCats == null)
			return null;

		RTTStatusPoint rttStatusPoint = getRTTStatusPoint(CATSREFERRAL_CANCELLED_BY_PROVIDER_NAT_CODE);
		Object mos = getMosUser();
		MemberOfStaff doMos = null;

		if (mos instanceof MemberOfStaffLiteVo)
		{
			doMos = MemberOfStaffLiteVoAssembler.extractMemberOfStaff(getDomainFactory(), (MemberOfStaffLiteVo) mos);
		}

		PathwayRTTStatus pathwayRTTStatus = new PathwayRTTStatus();
		pathwayRTTStatus.setRTTStatus(rttStatusPoint);
		pathwayRTTStatus.setStatusBy(doMos);
		pathwayRTTStatus.setStatusDateTime(eventDateTime);

		if (doCats.getJourney() != null && doCats.getJourney().getCurrentClock() != null)
		{
			doCats.getJourney().getCurrentClock().setCurrentRTTStatus(pathwayRTTStatus);

			if (doCats.getJourney().getCurrentClock().getRTTStatusHistory() == null)
				doCats.getJourney().getCurrentClock().setRTTStatusHistory(new java.util.ArrayList());

			doCats.getJourney().getCurrentClock().getRTTStatusHistory().add(pathwayRTTStatus);
		}

		return pathwayRTTStatus;
	}

	private RTTStatusPoint getRTTStatusPoint(int nationalCode)
	{
		String query = "select rtt from RTTStatusPoint as rtt where rtt.nationalCode = :NationalCode";

		List<?> rttList = getDomainFactory().find(query, new String[] { "NationalCode" }, new Object[] { nationalCode });

		if (rttList != null && rttList.size() > 0 && rttList.get(0) instanceof RTTStatusPoint)
		{
			return (RTTStatusPoint) rttList.get(0);
		}

		return null;
	}

	private void populateOldFields(Booking_Appointment doBookAppt, Appointment_Status newStatus)
	{
		newStatus.setStatusReason(doBookAppt.getCurrentStatusRecord().getStatusReason());
		newStatus.setApptDate(doBookAppt.getCurrentStatusRecord().getApptDate());
		newStatus.setApptTime(doBookAppt.getCurrentStatusRecord().getApptTime());
		newStatus.setPASClinic(doBookAppt.getCurrentStatusRecord().getPASClinic());
		newStatus.setDoS(doBookAppt.getCurrentStatusRecord().getDoS());
		newStatus.setPriority(doBookAppt.getCurrentStatusRecord().getPriority());
		newStatus.setCancellationReason(doBookAppt.getCurrentStatusRecord().getCancellationReason());
		newStatus.setComment(doBookAppt.getCurrentStatusRecord().getComment());
		newStatus.setRebookSelected(doBookAppt.getCurrentStatusRecord().isRebookSelected());
		newStatus.setUniqueLineRefNo(doBookAppt.getCurrentStatusRecord().getUniqueLineRefNo());
		newStatus.setWasOutputtedToWeeklyReport(doBookAppt.getCurrentStatusRecord().isWasOutputtedToWeeklyReport());
		newStatus.setWasOutputtedToMonthlyReport(doBookAppt.getCurrentStatusRecord().isWasOutputtedToMonthlyReport());
		newStatus.setEarliestOfferedDate(doBookAppt.getCurrentStatusRecord().getEarliestOfferedDate());
		//WDEV-23185
		if (doBookAppt.getSession() != null)
		{
			newStatus.setSession(doBookAppt.getSession());
		} //WDEV-23185
	}

	public CatsReferralProviderCancellationVo getCatsReferral(CatsReferralRefVo voRef)
	{
		if (voRef == null)
			throw new DomainRuntimeException("Cannot get CatsReferralProviderCancellationVo for null CatsReferralRefVo");

		return CatsReferralProviderCancellationVoAssembler.create((CatsReferral) getDomainFactory().getDomainObject(CatsReferral.class, voRef.getID_CatsReferral()));
	}

	public Date getReferralDate(CatsReferralRefVo referral)
	{
		if (referral == null || (referral != null && referral.getID_CatsReferral() == null))
			throw new DomainRuntimeException("Logical error - can not get CatsReferralProviderCancellation for null CatsReferralRefVo");

		CatsReferral catsReferral = (CatsReferral) getDomainFactory().getDomainObject(CatsReferral.class, referral.getID_CatsReferral());

		if (catsReferral != null && catsReferral.getReferralDetails() != null && catsReferral.getReferralDetails().getDateOfReferral() != null)// wdev-11045
			return new Date(catsReferral.getReferralDetails().getDateOfReferral());
		else
			return null;
	}

}
