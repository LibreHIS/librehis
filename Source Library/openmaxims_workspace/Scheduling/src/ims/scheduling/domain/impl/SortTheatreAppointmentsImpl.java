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
// This code was generated by Daniel Laffan using IMS Development Environment (version 1.70 build 3467.22451)
// Copyright (C) 1995-2009 IMS MAXIMS. All rights reserved.

package ims.scheduling.domain.impl;

import ims.domain.DomainFactory;
import ims.domain.exceptions.StaleObjectException;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.utils.Date;
import ims.framework.utils.Time;
import ims.scheduling.domain.SessionAdmin;
import ims.scheduling.domain.base.impl.BaseSortTheatreAppointmentsImpl;
import ims.scheduling.domain.objects.Booking_Appointment;
import ims.scheduling.domain.objects.FutureAppointmentDetails;
import ims.scheduling.domain.objects.Sch_Session_Appointment_Order;
import ims.scheduling.domain.objects.SessionParentChildSlot;
import ims.scheduling.domain.objects.SessionSlotStatus;
import ims.scheduling.domain.objects.SessionTheatreTCISlot;
import ims.scheduling.vo.BookingAppointmentTheatreVo;
import ims.scheduling.vo.BookingAppointmentTheatreVoCollection;
import ims.scheduling.vo.Booking_AppointmentVo;
import ims.scheduling.vo.Sch_SessionRefVo;
import ims.scheduling.vo.Sch_Session_Appointment_OrderVo;
import ims.scheduling.vo.SessionLiteVoCollection;
import ims.scheduling.vo.SessionParentChildSlotVo;
import ims.scheduling.vo.SessionTheatreTCISlotLiteVo;
import ims.scheduling.vo.domain.BookingAppointmentTheatreVoAssembler;
import ims.scheduling.vo.domain.FutureAppointmentDetailsVoAssembler;
import ims.scheduling.vo.domain.Sch_Session_Appointment_OrderVoAssembler;
import ims.scheduling.vo.domain.SessionLiteVoAssembler;
import ims.scheduling.vo.domain.SessionParentChildSlotVoAssembler;
import ims.scheduling.vo.domain.SessionTheatreTCISlotLiteVoAssembler;
import ims.scheduling.vo.lookups.SchProfileType;
import ims.scheduling.vo.lookups.Status_Reason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SortTheatreAppointmentsImpl extends BaseSortTheatreAppointmentsImpl implements ims.scheduling.domain.SortFixedTheatreAppointments
{
	private static final long serialVersionUID = 1L;

	/**
	* saveSessionApptOrder
	*/
	public void saveSessionApptOrder(ims.scheduling.vo.Sch_Session_Appointment_OrderVo voSessionApptOrder) throws ims.domain.exceptions.StaleObjectException
	{
		if (voSessionApptOrder == null)
			throw new CodingRuntimeException("sessionApptOrder is null in method saveSessionApptOrder");
		if (!voSessionApptOrder.isValidated())
			throw new CodingRuntimeException("sessionApptOrder has not been validated in method saveSessionApptOrder");
		
		//WDEV-11777 - need to update SessionTheatreSlots if needed with booked/slotopened etc. and maintain history
		BookingAppointmentTheatreVoCollection voCollChangedAppts = null;
		List oldSlots = new ArrayList();
		if(voSessionApptOrder.getAppointmentsIsNotNull())
		{
			voCollChangedAppts = new BookingAppointmentTheatreVoCollection();
			for(BookingAppointmentTheatreVo voAppt : voSessionApptOrder.getAppointments())
			{
				//Session Theatre Slot has beeen changed
				if(voAppt.getChangeSlotFromIsNotNull())
				{
					SessionTheatreTCISlotLiteVo voSlot = voAppt.getChangeSlotFrom();
					SessionTheatreTCISlot doChangeFromSlot = (SessionTheatreTCISlot)getDomainFactory().getDomainObject(voSlot);
					
					//re-open the slot that is not used now
					SessionAdmin impl = (SessionAdmin) getDomainImpl(SessionAdminImpl.class);
					doChangeFromSlot = impl.reOpenTheatreTCISlot(doChangeFromSlot);		
					
					oldSlots.add(doChangeFromSlot);
					voCollChangedAppts.add(voAppt);
				}
			}
		}
		
		Sch_Session_Appointment_Order doSessApptOrder = Sch_Session_Appointment_OrderVoAssembler.extractSch_Session_Appointment_Order(getDomainFactory(), voSessionApptOrder);
		//go through domain object list and find new appts (attached to different slot)
		Iterator it = doSessApptOrder.getAppointments().iterator();
		while(it.hasNext())
		{
		 	Booking_Appointment doAppt = (Booking_Appointment) it.next();
		 	//if the appt has been changed then we need to book into the new slot
		 	if(voCollChangedAppts != null)
		 	{
			 	for(BookingAppointmentTheatreVo voAppt : voCollChangedAppts)
			 	{
			 		if(doAppt.getId().equals(voAppt.getID_Booking_Appointment()))
			 			doAppt.setTheatreSlot(bookAppointmentIntoTheatreSlot(doAppt, SessionTheatreTCISlotLiteVoAssembler.extractSessionTheatreTCISlot(getDomainFactory(), voAppt.getTheatreSlot())));
			 	}
		 	}
		}
		
		getDomainFactory().save(doSessApptOrder);
		
		//commit the changed from slot changes to the database
		Iterator it1 = oldSlots.iterator();
		while(it1.hasNext())
		{
			SessionTheatreTCISlot doSlot = (SessionTheatreTCISlot) it1.next();
			getDomainFactory().save(doSlot);
	 	}
		
		
		closeRemainingSlots(doSessApptOrder);
	}

	/**
	 * @param doSessApptOrder
	 */
	private void closeRemainingSlots(Sch_Session_Appointment_Order doSessApptOrder) throws StaleObjectException
	{
		//WDEV-12918 
		//get all the slots in the session and set them to Closed and null their appointment values
		//associate the new appointments and times with valid slots and time
		List doLstAppts = new ArrayList();
		if(doSessApptOrder != null && doSessApptOrder.getSession() != null && doSessApptOrder.getSession().getParentChildSlots() != null)
		{
			Iterator itPCSlots = doSessApptOrder.getSession().getParentChildSlots().iterator();
			while(itPCSlots.hasNext())
			{
				SessionParentChildSlot doSlot = (SessionParentChildSlot) itPCSlots.next();
				if(doSlot.getAppointment() != null)
				{
					doLstAppts.add(doSlot.getAppointment());
					doSlot.getAppointment().setParentChildSlot(null);
					doSlot.setAppointment(null);
					
				}
				
				doSlot.setStatus(getDomLookup(Status_Reason.CLOSED));
				doSlot.setStatusReason(getDomLookup(Status_Reason.CLOSED));
				doSlot.setParentSlot(null);
			}
		}
		
		Iterator itAppts = doLstAppts.iterator();
		while(itAppts.hasNext())
		{
			Booking_Appointment doAppt = (Booking_Appointment) itAppts.next();
			if(doSessApptOrder.getSession() != null)
			{
				doAppt.setParentChildSlot(findOrCreateRelevantSlotinSession(doAppt, doSessApptOrder.getSession().getParentChildSlots()));
				if(doAppt.getParentChildSlot() != null)
					doAppt.getParentChildSlot().setAppointment(doAppt);
			}
			getDomainFactory().save(doAppt);	
		}
		
		 getDomainFactory().save(doSessApptOrder);
	}

	
	private SessionParentChildSlot findOrCreateRelevantSlotinSession(Booking_Appointment doAppt, Set parentChildSlots)
	{
		if(doAppt == null)
			throw new CodingRuntimeException("doAppt cannot be null in method findOrCreateRelevantSlotinSession");
		if (parentChildSlots == null)
			throw new CodingRuntimeException("parentChildSlots cannot be null in method findOrCreateRelevantSlotinSession");
		
		////////////////////////////////
		//sorting the set
		
		List items = new ArrayList();
		Iterator it = parentChildSlots.iterator();
		while(it.hasNext())
		{
			items.add((SessionParentChildSlot)it.next());
		}
		
		//sort the list by start time (custom comparator)
		Collections.sort(items, new Comparator<SessionParentChildSlot>() 
		{
			private int direction = 1;
		
			public int compare(SessionParentChildSlot ob1, SessionParentChildSlot ob2) 
			{
				if(ob1.getStartTime() != null )
					return  ob1.getStartTime().compareToIgnoreCase(ob2.getStartTime())*direction;
				if(ob1.getStartTime() != null)
					return (-1)*direction;
				
				return 0;
			}
		});
		
		//end list sort
		////////////////////////////
		
		SessionParentChildSlot doSlotToReturn = null;
		Iterator itSortedSlots = items.iterator();
		Time proposedApptTime = new Time(doAppt.getApptStartTime());
		while(itSortedSlots.hasNext())
		{			
			SessionParentChildSlot doSessParentChildSlot = (SessionParentChildSlot) itSortedSlots.next();
			Time proposedSlotTime = new Time(doSessParentChildSlot.getStartTime());
			
			if(proposedSlotTime.isLessOrEqualThan(proposedApptTime))
			{
				if(doSessParentChildSlot.getAppointment() == null)
					doSlotToReturn = doSessParentChildSlot;
			}
			else
				break;
		}
		
		if(doSlotToReturn != null)
		{
			//set the slot time to the new appt time
			doSlotToReturn.setStartTime(doAppt.getApptStartTime());
			doSlotToReturn.setStatus(getDomLookup(Status_Reason.APPOINTMENT_BOOKED));
			doSlotToReturn.setStatusReason(getDomLookup(Status_Reason.APPOINTMENTMOVED));
		}
		
		return doSlotToReturn;
	}

	public Sch_Session_Appointment_OrderVo getSessionApptOrderBySession(Sch_SessionRefVo session)
	{
		if (session == null || session.getID_Sch_Session() == null)
			throw new CodingRuntimeException("session is null or id not provided in method getSessionApptOrderBySession");
		
		Sch_Session_Appointment_Order doSessApptOrder = Sch_Session_Appointment_Order.getSch_Session_Appointment_OrderFromSession(getDomainFactory() , session.getID_Sch_Session());
		if(doSessApptOrder != null)
			return Sch_Session_Appointment_OrderVoAssembler.create(doSessApptOrder); 
			
		return null;
	}

	//list theatre sessions by date
	public SessionLiteVoCollection listSessionByDate(Date date)
	{
		if (date == null)
			throw new CodingRuntimeException("date is null in method listSessionByDate");
		
		List sessions = getDomainFactory().find("from Sch_Session as session where session.sessionDate = :sessionDate and session.sessionProfileType.id = :THEATRE_SESSION", 
				new String[]{"sessionDate", "THEATRE_SESSION"}, new Object[]{date.getDate(), SchProfileType.THEATRE.getID()});
		
		return SessionLiteVoAssembler.createSessionLiteVoCollectionFromSch_Session(sessions);
	}
	
	/**
	 * @param doAppt
	 * @param doSessionTheatreSlot
	 */
	private SessionTheatreTCISlot bookAppointmentIntoTheatreSlot(Booking_Appointment doAppt, SessionTheatreTCISlot doSessionTheatreSlot)
	{
		if (doSessionTheatreSlot == null)
			throw new CodingRuntimeException("doSessionTheatreSlot is null in method bookAppointmentIntoTheatreSlot");
		if (doAppt == null)
			throw new CodingRuntimeException("doAppt is null in method bookAppointmentIntoTheatreSlot");
		
		doAppt.setApptStartTime(doSessionTheatreSlot.getToComeInTime());

		//WDEV-12170
		if(doAppt.getCurrentStatusRecord() != null)
			doAppt.getCurrentStatusRecord().setApptTime(doSessionTheatreSlot.getToComeInTime()); 
		
		doSessionTheatreSlot.setAppointment(doAppt);

		doSessionTheatreSlot.setStatus(getDomLookup(Status_Reason.BOOKED));
		doSessionTheatreSlot.setStatusReason(getDomLookup(Status_Reason.BOOKED));
		
		SessionSlotStatus doStat = new SessionSlotStatus();
		doStat.setDateTime(new java.util.Date());
		doStat.setStatus(doSessionTheatreSlot.getStatus());
		doStat.setStatusReason(doSessionTheatreSlot.getStatusReason());
		doSessionTheatreSlot.getStatusReasonHistory().add(doStat);
		
		return doSessionTheatreSlot;
	}

	@Override
	public void saveReOrderedSession(Sch_Session_Appointment_OrderVo sessionApptOrder) throws StaleObjectException 
	{
		if (sessionApptOrder == null)
			throw new CodingRuntimeException("sessionApptOrder is null in method saveSessionApptOrder");
		if (!sessionApptOrder.isValidated())
			throw new CodingRuntimeException("sessionApptOrder has not been validated in method saveSessionApptOrder");
		
		DomainFactory factory = getDomainFactory();
		//Commit changed Bookings, we will save all as the slot time could all have changed
		Sch_Session_Appointment_Order doSessApptOrder = Sch_Session_Appointment_OrderVoAssembler.extractSch_Session_Appointment_Order(getDomainFactory(), sessionApptOrder);

		Iterator it = doSessApptOrder.getAppointments().iterator();
		while(it.hasNext())
		{
		 	Booking_Appointment doAppt = (Booking_Appointment) it.next();

		 	for(BookingAppointmentTheatreVo voAppt : sessionApptOrder.getAppointments())
			{
				if(doAppt.getId().equals(voAppt.getID_Booking_Appointment()))
				{
					SessionTheatreTCISlot doTCISlot = SessionTheatreTCISlotLiteVoAssembler.extractSessionTheatreTCISlot(getDomainFactory(), voAppt.getTheatreSlot());
					if (doTCISlot != null )
						doAppt.setTheatreSlot(bookAppointmentIntoTheatreSlot(doAppt, doTCISlot));
				}
		 	}
		}
		

		getDomainFactory().save(doSessApptOrder);
	}
}
