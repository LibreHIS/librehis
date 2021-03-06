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

package ims.RefMan.domain;

// Generated from form domain impl
public interface TheatreAdmissionDialog extends ims.domain.DomainInterface
{
	// Generated from form domain interface definition
	public ims.core.vo.LocShortMappingsVoCollection listActiveWardsForHospital(ims.core.resource.place.vo.LocationRefVo hospital);

	// Generated from form domain interface definition
	public ims.core.vo.CareContextShortVo admitPatient(ims.core.vo.PatientShort patVo, ims.core.vo.InpatientEpisodeVo episVo, ims.scheduling.vo.Booking_AppointmentRefVo bookingApptVoRef, ims.RefMan.vo.CatsReferralWizardVo voCats, Boolean readmitted, ims.RefMan.vo.PatientElectiveListBedAdmissionVo patientElectiveList, ims.RefMan.vo.PatientElectiveListBedAdmissionVoCollection patientElectiveListToBeCancelled) throws ims.domain.exceptions.DomainInterfaceException, ims.domain.exceptions.StaleObjectException, ims.domain.exceptions.ForeignKeyViolationException, ims.domain.exceptions.UniqueKeyViolationException;

	// Generated from form domain interface definition
	/**
	* listActiveMedics
	*/
	public ims.core.vo.MedicLiteVoCollection listActiveMedics(String szName);

	// Generated from form domain interface definition
	public ims.RefMan.vo.CatsReferralWizardVo getCatsReferralWizardVoForCareContext(ims.core.admin.vo.CareContextRefVo voCarecontext);

	// Generated from form domain interface definition
	/**
	* getCatsReferral
	*/
	public ims.RefMan.vo.CatsReferralWizardVo getCatsReferral(ims.scheduling.vo.Booking_AppointmentRefVo voBookingRefVo);

	// Generated from form domain interface definition
	public ims.scheduling.vo.Booking_AppointmentVo getBookingAppt(ims.scheduling.vo.Booking_AppointmentRefVo bookingApptRefVo);

	// Generated from form domain interface definition
	public Boolean isNotACurrentInpatient(ims.core.patient.vo.PatientRefVo voPatRef);

	// Generated from form domain interface definition
	public ims.core.vo.lookups.Specialty getReferralSpecialty(ims.scheduling.vo.Booking_AppointmentRefVo appointment);

	// Generated from form domain interface definition
	public ims.core.vo.LocShortVo getCurrentHospital(ims.framework.interfaces.ILocation currentLocation);

	// Generated from form domain interface definition
	public ims.core.vo.LocShortMappingsVo getBookingApptSessionLocation(Integer apptId);

	// Generated from form domain interface definition
	public ims.RefMan.vo.PatientElectiveListBedAdmissionVo getPatientElectiveListForAppointment(ims.scheduling.vo.Booking_AppointmentRefVo appointment);

	// Generated from form domain interface definition
	public ims.RefMan.vo.PatientElectiveListBedAdmissionVoCollection getDifferentPatientElectiveListForService(ims.core.patient.vo.PatientRefVo patient, ims.RefMan.vo.PatientElectiveListRefVo patientElectiveList, ims.core.clinical.vo.ServiceRefVo service);

	// Generated from form domain interface definition
	public Boolean hasPatientElectiveListsToBeCancelled(ims.core.patient.vo.PatientRefVo patient, ims.RefMan.vo.PatientElectiveListRefVo patientElectiveList, ims.core.clinical.vo.ServiceRefVo service);
}
