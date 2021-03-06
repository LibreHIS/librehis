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

package ims.core.domain;

// Generated from form domain impl
public interface AdmitToWard extends ims.domain.DomainInterface
{
	// Generated from form domain interface definition
	public ims.core.vo.ServiceLiteVoCollection listServices(String serviceName);

	// Generated from form domain interface definition
	/**
	* Retrieve consultants for service based on name.
	*/
	public ims.core.vo.MedicLiteVoCollection listConsultants(ims.core.clinical.vo.ServiceRefVo service, String name);

	// Generated from form domain interface definition
	public ims.core.vo.MedicLiteVoCollection listReferringConsultants(String name);

	// Generated from form domain interface definition
	public ims.core.vo.PatientLite_IdentifiersVoCollection listPatients(ims.core.vo.lookups.PatIdType idType, String idValue);

	// Generated from form domain interface definition
	public ims.core.vo.PatientLite_IdentifiersVoCollection listPatients(String surname, String forename);

	// Generated from form domain interface definition
	public ims.RefMan.vo.BookAppointmentForWardAttendersVoCollection listWardAttenders(ims.core.resource.place.vo.LocationRefVo ward, ims.framework.utils.Date date);

	// Generated from form domain interface definition
	public ims.RefMan.vo.PatientElectiveListBedInfoVoCollection listPatientElectives(ims.framework.utils.Date fromDate, ims.framework.utils.Date toDate, ims.core.resource.place.vo.LocationRefVo ward, ims.core.resource.place.vo.LocationRefVo hospital);

	// Generated from form domain interface definition
	public ims.core.vo.TrackingForPendingEmergencyAdmitVoCollection listPendingEDAdmission(ims.core.resource.place.vo.LocationRefVo location);

	// Generated from form domain interface definition
	public ims.core.vo.CatsReferralPendingEmergencyNonEDAdmissionListVoCollection listPendingEmergencies(ims.framework.utils.Date dteFrom, ims.framework.utils.Date dteTo, ims.core.resource.place.vo.LocationRefVo ward, ims.core.resource.place.vo.LocationRefVo hospital);

	// Generated from form domain interface definition
	public ims.core.vo.LocationLiteVo getLocationLite(ims.core.resource.place.vo.LocationRefVo location);

	// Generated from form domain interface definition
	public ims.core.vo.MedicLiteVo getMedic(ims.core.resource.people.vo.MemberOfStaffRefVo memberOfStaff);

	// Generated from form domain interface definition
	public ims.core.vo.MedicVo getMedicFull(ims.core.resource.people.vo.MedicRefVo medic);

	// Generated from form domain interface definition
	public ims.RefMan.vo.PatientElectiveListBedAdmissionVo getPatientElectiveList(ims.RefMan.vo.PatientElectiveListRefVo patientElectiveList);

	// Generated from form domain interface definition
	public ims.core.vo.PatientShort getPatient(ims.core.patient.vo.PatientRefVo patient);

	// Generated from form domain interface definition
	public String checkForPatientAlreadyAdmited(ims.core.patient.vo.PatientRefVo patient);

	// Generated from form domain interface definition
	public Boolean isPatientAlive(ims.core.patient.vo.PatientRefVo patient);

	// Generated from form domain interface definition
	public ims.core.vo.PendingEmergencyAdmissionAdmitVo automaticDischarge(ims.core.admin.pas.vo.PendingEmergencyAdmissionRefVo pendingEmergencyAdmission, ims.framework.utils.DateTime admissionDateTime, ims.core.vo.HcpLiteVo consultant) throws ims.domain.exceptions.StaleObjectException;

	// Generated from form domain interface definition
	public Boolean saveAdmissionAndUpdateList(ims.core.vo.AdmissionDetailVo admissionDetail, ims.core.vo.WardStayVo wardStay, ims.core.vo.ConsultantStayVo consultantStay, ims.core.vo.CareSpellAdmitVo careSpell, ims.core.vo.CatsReferralEmergencyAdmissionVo referral, Object selectedAdmissionData, ims.core.vo.PatientCaseNoteTransferVoCollection caseNoteTransfers) throws ims.domain.exceptions.DomainInterfaceException, ims.domain.exceptions.StaleObjectException, ims.domain.exceptions.UniqueKeyViolationException;

	// Generated from form domain interface definition
	public Boolean hasPatientActiveSelfAdmit(ims.core.patient.vo.PatientRefVo patient);

	// Generated from form domain interface definition
	public ims.core.vo.PatientCaseNoteVoCollection getCaseNoteFolders(ims.core.patient.vo.PatientRefVo patient);

	// Generated from form domain interface definition
	public ims.core.vo.LocationLiteVo getHospitalForLocation(ims.core.resource.place.vo.LocationRefVo location);

	// Generated from form domain interface definition
	public ims.core.vo.HcpLiteVo getHcpFromIMos(ims.vo.interfaces.IMos iMos);

	// Generated from form domain interface definition
	public ims.core.vo.CareSpellAdmitVo getCareSpellForAdmission(ims.core.admin.vo.CareContextRefVo careContext);

	// Generated from form domain interface definition
	public ims.core.vo.CareSpellAdmitVo getCareSpellForAdmission(ims.RefMan.vo.CatsReferralRefVo referral);

	// Generated from form domain interface definition
	public ims.core.vo.CareSpellAdmitVo getCareSpellForAdmission(ims.scheduling.vo.Booking_AppointmentRefVo appointment);

	// Generated from form domain interface definition
	public ims.core.vo.CatsReferralEmergencyAdmissionVo getReferralForAdmission(ims.RefMan.vo.CatsReferralRefVo referral);

	// Generated from form domain interface definition
	public ims.core.vo.CatsReferralEmergencyAdmissionVo getReferralForAdmission(ims.scheduling.vo.Booking_AppointmentRefVo appointment);

	// Generated from form domain interface definition
	public ims.core.resource.place.vo.LocationRefVo getHospitalForWard(ims.core.resource.place.vo.LocationRefVo wardRef);

	// Generated from form domain interface definition
	public Boolean patientHasTCIWithinNextMonth(ims.core.patient.vo.PatientRefVo patient);

	// Generated from form domain interface definition
	public Boolean patientHasAppointmentsWithinNextMonth(ims.core.patient.vo.PatientRefVo patient);

	// Generated from form domain interface definition
	public ims.core.vo.PatientLite_IdentifiersVo getPatientLite(ims.core.patient.vo.PatientRefVo patientRef);

	// Generated from form domain interface definition
	public ims.core.vo.CareContextShortVo getCareContextShort(ims.core.patient.vo.PatientRefVo patRef);

	// Generated from form domain interface definition
	public ims.core.vo.PatientShort getPatientShort(ims.core.patient.vo.PatientRefVo patRef);

	// Generated from form domain interface definition
	public ims.core.vo.PatientWithGPForCCGVo getPatientForCCG(ims.core.patient.vo.PatientRefVo patientRef);

	// Generated from form domain interface definition
	public String getCodeCCGFromPostalCode(String postCode);

	// Generated from form domain interface definition
	public String getCCGCodeForCurrentLocationOrg(ims.framework.interfaces.ILocation iCurrentLoc);

	// Generated from form domain interface definition
	/**
	* gets the admission detail for the PAS event
	*/
	public ims.core.vo.AdmissionDetailVo getAdmissionDetail(ims.core.vo.PasEventAdmitVo pasEvent);

	// Generated from form domain interface definition
	public Boolean isCaseNoteFolderLocation(ims.core.patient.vo.PatientRefVo patientRef);
}
