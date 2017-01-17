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
// This code was generated by George Cristian Josan using IMS Development Environment (version 1.65 build 3225.30788)
// Copyright (C) 1995-2008 IMS MAXIMS plc. All rights reserved.

package ims.core.domain.impl;

import ims.core.admin.domain.objects.CareContext;
import ims.core.admin.vo.CareContextRefVo;
import ims.core.domain.base.impl.BaseTransportImpl;
import ims.core.patient.domain.objects.Patient;
import ims.core.patient.vo.PatientRefVo;
import ims.core.vo.CareContextShortVo;
import ims.core.vo.PatientShort;
import ims.core.vo.domain.CareContextShortVoAssembler;
import ims.core.vo.domain.PatientShortAssembler;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;
import ims.framework.utils.Date;
import ims.scheduling.domain.objects.Booking_Appointment;
import ims.scheduling.vo.Booking_AppointmentRefVo;
import ims.scheduling.vo.Booking_AppointmentTransportVo;
import ims.scheduling.vo.Booking_AppointmentTransportVoCollection;
import ims.scheduling.vo.domain.Booking_AppointmentTransportVoAssembler;

import java.util.ArrayList;


public class TransportImpl extends BaseTransportImpl
{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Booking_AppointmentTransportVoCollection listAppoinments(Date startDate, Date endDate, Boolean displayBookedAppointments) throws ims.domain.exceptions.DomainInterfaceException
	{
		if (startDate == null)
			throw new DomainInterfaceException("Start Date must be provided.");
		
		DomainFactory factory = getDomainFactory();
		
		StringBuilder hqlQuery = new StringBuilder("SELECT DISTINCT appointment FROM Booking_Appointment AS appointment LEFT JOIN appointment.apptStatus AS status");

		/* TODO MSSQL case - hqlQuery.append(" WHERE appointment.isTransportRequired = 1 "); */
		hqlQuery.append(" WHERE appointment.isTransportRequired = true ");
		
		if (!displayBookedAppointments) {
            hqlQuery.append(" AND (appointment.isTransportBooked is null OR appointment.isTransportBooked = 0) AND status.id = -1469 ");
        } else {
		    /* TODO MSSQL case - hqlQuery.append(" AND appointment.isTransportBooked = 1 AND status.id = -1469"); */
            hqlQuery.append(" AND appointment.isTransportBooked = true AND status.id = -1469");
        }
		
		ArrayList<String> paramNames = new ArrayList<String>();
		ArrayList<Object> paramValues = new ArrayList<Object>();
		
		if (endDate == null)
		{
			hqlQuery.append(" AND appointment.appointmentDate >= :appointmentDate");
			paramNames.add("appointmentDate");
			paramValues.add(startDate.getDate());
		}
		else
		{
			hqlQuery.append(" AND appointment.appointmentDate BETWEEN :startDate AND :endDate");
			paramNames.add("startDate");
			paramNames.add("endDate");
			paramValues.add(startDate.getDate());
			paramValues.add(endDate.getDate());
		}
		
		hqlQuery.append(" ORDER BY appointment.appointmentDate, appointment.apptStartTime");
		
		return Booking_AppointmentTransportVoAssembler.createBooking_AppointmentTransportVoCollectionFromBooking_Appointment(factory.find(hqlQuery.toString(), paramNames, paramValues));
	}


	public Booking_AppointmentTransportVo getTransportBooking(Booking_AppointmentRefVo appointmentRef) throws ims.domain.exceptions.DomainInterfaceException
	{
		if (appointmentRef == null)
			throw new DomainInterfaceException("An appointment must be selected");
		
		DomainFactory factory = getDomainFactory();
		
		return Booking_AppointmentTransportVoAssembler.create((Booking_Appointment) factory.getDomainObject(Booking_Appointment.class, appointmentRef.getID_Booking_Appointment()));
	}


	public PatientShort getPatient(PatientRefVo patient)
	{
		if (patient == null)
			return null;
		
		return PatientShortAssembler.create((Patient) getDomainFactory().getDomainObject(Patient.class, patient.getID_Patient()));
	}


	public CareContextShortVo getCareContext(CareContextRefVo careContext)
	{
		if (careContext == null)
			return null;
		
		return CareContextShortVoAssembler.create((CareContext) getDomainFactory().getDomainObject(CareContext.class, careContext.getID_CareContext()));
	}
}
