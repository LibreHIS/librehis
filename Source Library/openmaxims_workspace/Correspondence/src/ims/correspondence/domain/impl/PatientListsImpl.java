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
// This code was generated by Sean Nesbitt using IMS Development Environment (version 1.35 build 2097.21013)
// Copyright (C) 1995-2005 IMS MAXIMS plc. All rights reserved.


package ims.correspondence.domain.impl;

import ims.admin.domain.HcpAdmin;
import ims.admin.domain.OrganisationAndLocation;
import ims.admin.domain.impl.HcpAdminImpl;
import ims.admin.domain.impl.OrganisationAndLocationImpl;
import ims.configuration.gen.ConfigFlag;
import ims.core.admin.pas.domain.objects.PASEvent;
import ims.core.admin.pas.vo.PASEventRefVo;
import ims.core.domain.ADT;
import ims.core.domain.Demographics;
import ims.core.domain.impl.ADTImpl;
import ims.core.domain.impl.DemographicsImpl;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.people.vo.HcpRefVo;
import ims.core.resource.people.vo.MedicRefVo;
import ims.core.resource.place.vo.ClinicRefVo;
import ims.core.resource.place.vo.LocationRefVo;
import ims.core.vo.DocumentVo;
import ims.core.vo.InpatientEpisodeVo;
import ims.core.vo.InpatientEpisodeVoCollection;
import ims.core.vo.LocShortMappingsVo;
import ims.core.vo.LocShortMappingsVoCollection;
import ims.core.vo.LocShortVo;
import ims.core.vo.MedicLiteVoCollection;
import ims.core.vo.MedicWithMappingsLiteVo;
import ims.core.vo.PasEventVo;
import ims.core.vo.PasEventWithReferringGPVo;
import ims.core.vo.PatientListsFilterVo;
import ims.core.vo.PatientShort;
import ims.core.vo.domain.InpatientEpisodeVoAssembler;
import ims.core.vo.domain.PasEventVoAssembler;
import ims.core.vo.lookups.DocumentStatus;
import ims.core.vo.lookups.LocationType;
import ims.core.vo.lookups.PatIdType;
import ims.core.vo.lookups.Sex;
import ims.core.vo.lookups.Specialty;
import ims.core.vo.lookups.TaxonomyType;
import ims.correspondence.domain.CorrespondenceDetails;
import ims.correspondence.domain.NoLetterRequiredAdminConfig;
import ims.correspondence.domain.PatientLists;
import ims.correspondence.domain.UserProfile;
import ims.correspondence.vo.domain.ConsultantAccessFullVoAssembler;
import ims.correspondence.vo.domain.ClinicAccessFullVoAssembler;
import ims.correspondence.vo.domain.SpecialtyAccessVoAssembler;
import ims.correspondence.vo.ClinicAccessFullVoCollection;
import ims.correspondence.vo.ConsultantAccessFullVoCollection;
import ims.correspondence.vo.CorrespondenceDetailsRefVo;
import ims.correspondence.vo.CorrespondenceDetailsVo;
import ims.correspondence.vo.PasContactVo;
import ims.correspondence.vo.PasContactVoCollection;
import ims.correspondence.vo.SpecialtyAccessVoCollection;
import ims.correspondence.vo.UserAccessFullVo;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.exceptions.UniqueKeyViolationException;
import ims.dto.DTODomainImplementation;
import ims.dto.Result;
import ims.dto.client.Inpatientlist;
import ims.dto.client.Outpatientlist;
import ims.dto.client.Inpatientlist.InpatientlistRecord;
import ims.dto.client.Outpatientlist.OutpatientlistRecord;
import ims.framework.enumerations.SortOrder;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.utils.Date;
import ims.framework.utils.DateFormat;
import ims.framework.utils.DateTime;
import ims.framework.utils.PartialDate;
import ims.vo.LookupInstVo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PatientListsImpl extends DTODomainImplementation implements PatientLists, ims.domain.impl.Transactional
{
	/**
	 * 	 */
	private static final long serialVersionUID = 1L;

	public boolean dtoOnly() {
		return false;
	}

	public Class getLookupServiceClass()
	{
		return ims.domain.lookups.impl.LookupServiceImpl.class;
	}

	/**
	* Retrieves and replicates patient
	*/
	public ims.core.vo.Patient getPatient(ims.core.vo.PatientShort voPatientShort)throws ims.domain.exceptions.StaleObjectException
	{
		Demographics implDemographics = (Demographics)getDomainImpl(DemographicsImpl.class);
		return implDemographics.getPatient(voPatientShort);
	}

	/* (non-Javadoc)
	 * @see ims.dischargeletters.domain.InPatientList#listWards()
	 */
	public LocShortMappingsVoCollection listActiveWards(String name) 
	{ 
		OrganisationAndLocation impl = (OrganisationAndLocation) getDomainImpl(OrganisationAndLocationImpl.class);		
		return impl.listActiveWardsForHospital(null);
	}

	/* (non-Javadoc)
	 * @see ims.dischargeletters.domain.InPatientList#getPasEvent(ims.dischargeletters.vo.PasContactVo)
	 */


	public ims.correspondence.vo.CorrespondenceDetailsVo saveCorrespondenceDetails(CorrespondenceDetailsVo voDocumentDetails, LookupInstVo pasEventType) throws DomainInterfaceException, StaleObjectException, UniqueKeyViolationException 
	{
		CorrespondenceDetails impl = (CorrespondenceDetails) getDomainImpl(CorrespondenceDetailsImpl.class);
		return impl.saveCorrespondenceDetails(voDocumentDetails,pasEventType);
	}


	
	private void populatePasContactFromDtoInpatient(PasContactVo voPasContact, InpatientlistRecord dtoInpatient)
	{
		String extSystem = ConfigFlag.DOM.DEMOGRAPHICS_EXT_SYSTEM.getValue();			
		LocShortVo voLoc = new LocShortVo();		
		OrganisationAndLocation locImpl = (OrganisationAndLocation)getDomainImpl(OrganisationAndLocationImpl.class); 
		voLoc = locImpl.getLocationByLocalCode(dtoInpatient.Rescode,LocationType.WARD);
		voPasContact.setLocation(voLoc);
						
		Specialty specialty = (Specialty)getLookupService().getLocalLookup(Specialty.class, Specialty.TYPE_ID, extSystem, dtoInpatient.Specialty);					
		voPasContact.setSpecialty(specialty);			

		//Contact Number
		voPasContact.setContactNumber(dtoInpatient.Contactno);		
		try 
		{
			//Discharge Date
			voPasContact.setDischargeDateTime(dtoInpatient.Dischargedate == "" || dtoInpatient.Dischargedate == null ? null : new DateTime (dtoInpatient.Dischargedate));
		} 
		catch (ParseException e) 
		{
			voPasContact.setDischargeDateTime(null);
			e.printStackTrace();
		}
				
		PatientShort voPatientShort = new PatientShort();

		HcpAdmin hcpAdmin = (HcpAdmin)getDomainImpl(HcpAdminImpl.class);
		MedicWithMappingsLiteVo voMedicWithMappingsLite = hcpAdmin.getMedicMedicMappingsByPasCode(dtoInpatient.Hcpcode);
		voPasContact.setConsultant(voMedicWithMappingsLite == null ? null : voMedicWithMappingsLite);
		
		voPatientShort.setName(dtoInpatient.Snm, dtoInpatient.Fnm1, null);	

		voPatientShort.addId(PatIdType.HOSPNUM, dtoInpatient.Hospnum);						
		voPatientShort.addId(PatIdType.CHARTNUM, dtoInpatient.Chartnum);						

		voPatientShort.setDob(dtoInpatient.Dob == "" ? null : new PartialDate(dtoInpatient.Dob));	
		
		if(dtoInpatient.Dod != null && dtoInpatient.Dod != "")
		{
			try {voPatientShort.setDod(new Date(dtoInpatient.Dod,DateFormat.ISO));
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
		
		voPatientShort.calculateAge();
		voPatientShort.setWard(voPasContact.getLocation());		
		voPatientShort.setSex((Sex)getLookupService().getLocalLookup(Sex.class, Sex.TYPE_ID, extSystem, dtoInpatient.Sex));
		voPasContact.setPatient(voPatientShort);			
		
		voPasContact.setGPCode(dtoInpatient.Gpcode); 
		
		if (dtoInpatient.Documentstatus != ""  && Integer.valueOf(dtoInpatient.Documentstatus).intValue() == DocumentStatus.LETTERREQUIRED.getId())
		{ 
			voPasContact.setDocumentStatus((DocumentStatus)getLookupService().getLookupInstance(DocumentStatus.class,Integer.valueOf(dtoInpatient.Documentstatus).intValue()));
		}
		else				
		{
			if (isLetterRequired(voMedicWithMappingsLite, specialty ,null, null) == Boolean.FALSE)
				voPasContact.setDocumentStatus(DocumentStatus.NOLETTERREQUIRED);
			else
				voPasContact.setDocumentStatus(DocumentStatus.LETTERREQUIRED);
		}
	}


	public PasContactVoCollection listOutPatients(Date dteDischargeDateFrom, Date dteDischargeDateTo,  ClinicAccessFullVoCollection voCollClinicAccess, ConsultantAccessFullVoCollection voCollConsultantAccess, SpecialtyAccessVoCollection voCollSpecialtyAccess, DocumentStatus lkpDocumentStatus) throws DomainInterfaceException  
	{
		if (dteDischargeDateFrom == null)
			throw new CodingRuntimeException("Attendance Date is a mandatory argument");

		PasContactVoCollection voPasContactColl= new PasContactVoCollection();		
		Outpatientlist dtoOutpat = (Outpatientlist)getDTOInstance(Outpatientlist.class);
		dtoOutpat.Filter.clear();		
				
		String strHql = buildUserAccessHQL(null, dteDischargeDateFrom, dteDischargeDateTo, voCollClinicAccess, voCollConsultantAccess, voCollSpecialtyAccess, lkpDocumentStatus, Boolean.FALSE);
		dtoOutpat.Filter.Groupcond = strHql;
				
		Result result = null;		
		
		int maxContacts = ConfigFlag.DOM.CONTACT_SEARCH_MAX_SIZE.getValue();
		result=dtoOutpat.list(maxContacts);		
		
		if (result == null)
		{
			OutpatientlistRecord dtoOutpatRecord;
			PasContactVo voPasContact;
			for (int i = 0; i < dtoOutpat.DataCollection.count(); i++)
			{
				voPasContact = new PasContactVo();
				dtoOutpatRecord = dtoOutpat.DataCollection.get(i);						
				populatePasContactFromDtoOutpatient(voPasContact, dtoOutpatRecord);				
				
				if ((voPasContact.getDocumentStatusIsNotNull() && voPasContact.getDocumentStatus().equals(DocumentStatus.NOLETTERREQUIRED) == false))
				{
					Integer nDocStatus = dtoOutpatRecord.Documentstatus == null || dtoOutpatRecord.Documentstatus == "" ? new Integer(0) : new Integer(dtoOutpatRecord.Documentstatus);
					if (nDocStatus != null && nDocStatus.intValue() != 0)
					{				
						DocumentStatus lkpInPatDocumentStatus = (DocumentStatus)getLookupService().getLookupInstance(DocumentStatus.class, DocumentStatus.TYPE_ID,nDocStatus.intValue() ); 
						if (lkpInPatDocumentStatus != null)
							voPasContact.setDocumentStatus(lkpInPatDocumentStatus);
					}
					else
						voPasContact.setDocumentStatus(DocumentStatus.LETTERREQUIRED);
				}
				
				voPasContactColl.add(voPasContact);
			}
		}
		else
		{
			 throw new DomainInterfaceException(result.toString());
		}

		return voPasContactColl.sort();		
	}


	private String buildUserAccessHQL(LocShortMappingsVo voWard, Date dteDischargeDate, Date dteDischargeTo, ClinicAccessFullVoCollection voCollClinicAccess, ConsultantAccessFullVoCollection voCollConsultantAccess, SpecialtyAccessVoCollection voCollSpecialtyAccess, DocumentStatus lkpDocumentStatus, Boolean bInpatientList) throws DomainInterfaceException 
	{
		StringBuffer condStr = new StringBuffer();
		String strAnd = new String();
		String strLcode = "";
		
		if (voWard != null)
		{
			strLcode = null;
			strLcode = voWard.getPasCode();
			if (strLcode != null &&  strLcode != "")
			{
				condStr.append( strAnd + "$RESCODE$ = '" +  strLcode + "'" );
				strAnd = " AND ";				
			}
		}

		
		if (bInpatientList != null && !bInpatientList.booleanValue())
		{
			StringBuffer strClinic = new StringBuffer();			
			strClinic = buildClinicMessage(voCollClinicAccess, strAnd);
			if (strClinic != null && strClinic.length() > 0)
			{
				condStr.append(strClinic);
				strAnd = " AND ";
			}
		}
			
		StringBuffer strConsultant = new StringBuffer();
		strConsultant = buildConsultantMessage(voCollConsultantAccess, strAnd);
		if (strConsultant != null && strConsultant.length() > 0)
		{
			condStr.append(strConsultant);
			strAnd = " AND ";
		}
		
		
		StringBuffer strSpecialty = new StringBuffer();
		strSpecialty = buildSpecialtyMessage(voCollSpecialtyAccess, strAnd);
		if (strSpecialty != null && strSpecialty.length() > 0)
		{
			condStr.append(strSpecialty);
			strAnd = " AND ";
		}
					
		
		String strGroupCond = new String();		

		//WDEV-2773 
		if (dteDischargeDate != null)
		{
			if (dteDischargeTo != null)
			{
				Date dateFrom = dteDischargeDate;
				Date dateTo = dteDischargeTo;	
				if (bInpatientList != null && bInpatientList.booleanValue())
					strGroupCond = condStr + strAnd + "($DISCHARGEDATE$ >= to_date(" + dateFrom.toString(DateFormat.ISO) + ",'yyyymmdd') AND $DISCHARGEDATE$ <= to_date(" + dateTo.toString(DateFormat.ISO) + ", 'yyyymmdd') )";
				if (bInpatientList != null && !bInpatientList.booleanValue())
					strGroupCond = condStr + strAnd + "($APPTDATE$ >= to_date(" + dateFrom.toString(DateFormat.ISO) + ",'yyyymmdd') AND $APPTDATE$ <= to_date(" + dateTo.toString(DateFormat.ISO) + ", 'yyyymmdd') )";
				
				strAnd = " AND ";
			}
			else
			{
				Date dateFrom = dteDischargeDate;
				Date dateTo = dateFrom.copy();
				dateTo = dateTo.addDay(1);	
				if (bInpatientList != null && bInpatientList.booleanValue())
					strGroupCond = condStr + strAnd + "($DISCHARGEDATE$ >= to_date(" + dateFrom.toString(DateFormat.ISO) + ",'yyyymmdd') AND $DISCHARGEDATE$ <= to_date(" + dateTo.toString(DateFormat.ISO) + ", 'yyyymmdd') )";
				if (bInpatientList != null && !bInpatientList.booleanValue())
					strGroupCond = condStr + strAnd + "($APPTDATE$ >= to_date(" + dateFrom.toString(DateFormat.ISO) + ",'yyyymmdd') AND $APPTDATE$ <= to_date(" + dateTo.toString(DateFormat.ISO) + ", 'yyyymmdd') )";
				
				strAnd = " AND ";
			}
		}
		else
			strGroupCond = condStr.toString();
		
			
		if (lkpDocumentStatus != null)
		{
			if (lkpDocumentStatus.equals(DocumentStatus.LETTERREQUIRED))
			{
				/* TODO MSSQL case - String strStatusCond = "(($DOCUMENTSTATUS$ = 0 AND $DOCUMENTSTATUS$ != " + getDomLookup(DocumentStatus.NOLETTERREQUIRED).getId() + ") OR $DOCUMENTSTATUS$ = " + getDomLookup(DocumentStatus.LETTERREQUIRED).getId() + ")"; */
				String strStatusCond = "(($DOCUMENTSTATUS$ = FALSE AND $DOCUMENTSTATUS$ != " + getDomLookup(DocumentStatus.NOLETTERREQUIRED).getId() + ") OR $DOCUMENTSTATUS$ = " + getDomLookup(DocumentStatus.LETTERREQUIRED).getId() + ")";
				strGroupCond = strGroupCond + strAnd + strStatusCond;
			}				
			else
				strGroupCond = strGroupCond + strAnd + "($DOCUMENTSTATUS$ = " + getDomLookup(lkpDocumentStatus).getId() + ")";

		}
				
		return strGroupCond;	
	}

	

	private StringBuffer buildSpecialtyMessage(SpecialtyAccessVoCollection voCollSpecialtyAccess, String strAnd) throws DomainInterfaceException   
	{
		StringBuffer strSpecialtyCodes = new StringBuffer();
		StringBuffer strSpecialtyMessage = new StringBuffer();
		String strSpecialtyClause = null;		
		if (voCollSpecialtyAccess != null && voCollSpecialtyAccess.size() > 0)
		{
			String extSystem = ConfigFlag.DOM.DEMOGRAPHICS_EXT_SYSTEM.getValue();
			for (int i=0; i < voCollSpecialtyAccess.size(); i++) 
			{			
				strSpecialtyClause = getLookupService().getRemoteLookup(voCollSpecialtyAccess.get(i).getSpecialty().getId(), extSystem);
				if (strSpecialtyClause != null)
				{
					if (i != 0) strSpecialtyCodes.append(",");					
					
					strSpecialtyCodes.append("'" + strSpecialtyClause + "'");
				}				
			}
		}	

		
		/**
		 * The user has chosen a single Specialty to filter on but that Specialty has no 
		 * associated PAS code.
		 */
		if (voCollSpecialtyAccess != null)
		{
			if (strSpecialtyClause == null && voCollSpecialtyAccess.size() == 1)
			{
				String sErr = new String();
				sErr = "Specialty '" + voCollSpecialtyAccess.get(0).getSpecialty().getText() + "' has no associated PAS Code";
				throw new DomainInterfaceException(sErr);
			}
		}
		
		if (strSpecialtyCodes != null && strSpecialtyCodes.length() > 0)
			strSpecialtyMessage.append(strAnd+ "$SPECIALTY$ in ( " + strSpecialtyCodes + ")");
			
		return strSpecialtyMessage;
	}

	private StringBuffer buildConsultantMessage(ConsultantAccessFullVoCollection voCollConsultantAccess, String strAnd) throws DomainInterfaceException 
	{
		StringBuffer strConsultantCodes = new StringBuffer();
		StringBuffer strConsultantMessage = new StringBuffer();
		String strLcode = null;
		if (voCollConsultantAccess != null && voCollConsultantAccess.size() > 0)
		{											
			for (int i=0; i < voCollConsultantAccess.size(); i++)
			{
				if (voCollConsultantAccess.get(i) != null && voCollConsultantAccess.get(i).getConsultant() != null && voCollConsultantAccess.get(i).getConsultant().getMos() != null)
				{
					strLcode = voCollConsultantAccess.get(i).getConsultant().getMos().getPasCode();
					if ( strLcode != null)
					{ 
						if (i != 0) strConsultantCodes.append(",");
						
						strConsultantCodes.append( "'" +  strLcode + "'");	
					}
				}
			}
		}

		/**
		 * The user has chosen a single consultant to filter on but that consultant has no 
		 * associated PAS code.
		 */
		if (voCollConsultantAccess != null)
		{
			if (strLcode == null && voCollConsultantAccess.size() == 1)
			{
				String sErr = new String();
				sErr = "Consultant '" + voCollConsultantAccess.get(0).getConsultant().getMos().getName().toShortForm() + "' has no associated PAS Code";
				throw new DomainInterfaceException(sErr);
			}
		}
		
		if (strConsultantCodes != null && strConsultantCodes.length() > 0)
			strConsultantMessage.append(strAnd+ "$HCPCODE$ in ( " + strConsultantCodes + ")");
			
		return strConsultantMessage;
	}

	/**
	 * @param voCollClinicAccess
	 * @param strAnd
	 * @param strConsultant 
	 * @return
	 */
	private StringBuffer buildClinicMessage(ClinicAccessFullVoCollection voCollClinicAccess, String strAnd) throws DomainInterfaceException 
	{
		StringBuffer strClinicCodes = new StringBuffer();
		StringBuffer strClinicMessage = new StringBuffer();
		String strLcode = null;

		if (voCollClinicAccess != null)		
		{
			if (voCollClinicAccess.size() > 0)
			{
				for (int i = 0; i < voCollClinicAccess.size(); i++)
				{
					if (voCollClinicAccess.get(i) != null && voCollClinicAccess.get(i).getClinicIsNotNull())
					{
						strLcode = voCollClinicAccess.get(i).getClinic().getMapping(TaxonomyType.PAS);
						if (strLcode != null || strLcode != "")
						{
							if (i != 0)
								strClinicCodes.append(",");

							strClinicCodes.append("'" + strLcode + "'");
						}
					}
				}
			}
		
			/**
			 * The user has chosen a single specialty to filter on but that
			 * specialty has no associated PAS code.
			 */
			if (strLcode  == null && voCollClinicAccess.size() == 1)
			{
				String sErr = new String();
				sErr = "Clinic '" + voCollClinicAccess.get(0).getClinic().getClinicName() + "' has no associated PAS Code";
				throw new DomainInterfaceException(sErr);			
			}
			
			if (strClinicCodes != null && strClinicCodes.length() > 0)
				strClinicMessage.append(strAnd+ "$CLINICCODE$ in ( " + strClinicCodes + ")");
		}
		
		return strClinicMessage;
	}
		
	
	private void populatePasContactFromDtoOutpatient(PasContactVo voPasContact, OutpatientlistRecord dtoOutpatient)
	{
		String extSystem = ConfigFlag.DOM.DEMOGRAPHICS_EXT_SYSTEM.getValue();			
		LocShortVo voLoc = new LocShortVo();		
		OrganisationAndLocation locImpl = (OrganisationAndLocation)getDomainImpl(OrganisationAndLocationImpl.class);
		
		voLoc = locImpl.getLocationByLocalCode(dtoOutpatient.Cliniccode,LocationType.CLINIC);
		voPasContact.setLocation(voLoc);
						
		HcpAdmin hcpAdmin = (HcpAdmin)getDomainImpl(HcpAdminImpl.class);
		
		MedicWithMappingsLiteVo voMedicWithMappingsLite = hcpAdmin.getMedicMedicMappingsByPasCode(dtoOutpatient.Hcpcode);
		voPasContact.setConsultant(voMedicWithMappingsLite == null ? null : voMedicWithMappingsLite);
				

		Specialty specialty = (Specialty)getLookupService().getLocalLookup(Specialty.class, Specialty.TYPE_ID, extSystem, dtoOutpatient.Specialty);			
		
		voPasContact.setSpecialty(specialty);			
		
		//Contact Number
		voPasContact.setContactNumber(dtoOutpatient.Contactno);
		try {
			voPasContact.setAppointmentDateTime(new DateTime (dtoOutpatient.Apptdate, "1000"));
		} catch (ParseException e) {
			voPasContact.setAppointmentDateTime(null);
		}
						
		PatientShort voPatientShort = new PatientShort();
		voPatientShort.setName(dtoOutpatient.Snm, dtoOutpatient.Fnm1, null);	

		voPatientShort.addId(PatIdType.HOSPNUM, dtoOutpatient.Hospnum);						
		voPatientShort.addId(PatIdType.CHARTNUM, dtoOutpatient.Chartnum);						

		if(dtoOutpatient.Dob != null && dtoOutpatient.Dob != "")
			voPatientShort.setDob(new PartialDate(dtoOutpatient.Dob));	
		else
			voPatientShort.setDob(null);
		
		if(dtoOutpatient.Dod != null && dtoOutpatient.Dod != "")
		{
			try {voPatientShort.setDod(new Date(dtoOutpatient.Dod,DateFormat.ISO));
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
		
		if(dtoOutpatient.Dob != null && dtoOutpatient.Dob != "")
			voPatientShort.calculateAge();
		
		voPatientShort.setWard(voPasContact.getLocation());		
		voPatientShort.setSex((Sex)getLookupService().getLocalLookup(Sex.class, Sex.TYPE_ID, extSystem, dtoOutpatient.Sex));
		voPasContact.setPatient(voPatientShort);			
		voPasContact.setGPCode(dtoOutpatient.Gpcode);

		if (dtoOutpatient.Documentstatus != ""  && Integer.valueOf(dtoOutpatient.Documentstatus).intValue() == DocumentStatus.LETTERREQUIRED.getId())
		{ 
			voPasContact.setDocumentStatus((DocumentStatus)getLookupService().getLookupInstance(DocumentStatus.class,Integer.valueOf(dtoOutpatient.Documentstatus).intValue()));
		}
		else				
		{
			if (isLetterRequired(voMedicWithMappingsLite, specialty ,null, null) == Boolean.FALSE)
				voPasContact.setDocumentStatus(DocumentStatus.NOLETTERREQUIRED);
			else
				voPasContact.setDocumentStatus(DocumentStatus.LETTERREQUIRED);
		}		
	}

	public PasEventVo savePasEvent(PasEventVo voPasEvent) throws DomainInterfaceException, StaleObjectException 
	{
		if (!voPasEvent.isValidated())
		{
			throw new DomainRuntimeException("PasEventVo VO not validated.");
		}
		
		DomainFactory factory = getDomainFactory();	
		PASEvent domPasEvent = PasEventVoAssembler.extractPASEvent(factory,voPasEvent);
		
		factory.save(domPasEvent);
		
		return PasEventVoAssembler.create(domPasEvent);
	}

	public Boolean isLetterRequired(HcpRefVo voRefMedic, Specialty lkpSpecialty, ClinicRefVo voRefClinicNew, ClinicRefVo voRefClinicReview)  
	{
		NoLetterRequiredAdminConfig impl = (NoLetterRequiredAdminConfig) getDomainImpl(NoLetterRequiredAdminConfigImpl.class);
		return impl.isLetterRequired(voRefMedic, lkpSpecialty, voRefClinicNew, voRefClinicReview);
	}


	public PasContactVoCollection listInPatients(PatientListsFilterVo voSearchCriteria) throws DomainInterfaceException 
	{ 								
		if (ConfigFlag.UI.USE_PAS_CONTACT_FOR_CORRESPONDENCE.getValue())
		{			
			return listDTOInPatient(voSearchCriteria);			
		}
		else
		{
			return listPAtientPasEvents(voSearchCriteria);
		}
	}


	private PasContactVoCollection listPAtientPasEvents(PatientListsFilterVo voSearchCriteria) 
	{		
		DomainFactory factory = getDomainFactory();
		String hql;
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();
		
		hql = " from InpatientEpisode ip "; 
		StringBuffer condStr = new StringBuffer();
		String andStr = " ";
		if (voSearchCriteria.getLocationIsNotNull())
		{
			condStr.append(andStr + " ip.pasEvent.location.id = :ward");
			markers.add("ward");
			values.add(voSearchCriteria.getLocation().getID_Location());
			andStr = " and ";
		}
		else if (voSearchCriteria.getWardIsNotNull())
		{
			condStr.append(andStr + " ip.pasEvent.location.id = :ward");
			markers.add("ward");
			values.add(voSearchCriteria.getWard().getID_Location());
			andStr = " and ";			
		}
		
		if (voSearchCriteria.getConsultantIsNotNull())
		{						
			condStr.append(andStr + " ip.pasEvent.consultant.id = :cons");
			markers.add("cons");
			values.add(voSearchCriteria.getConsultant().getID_Hcp());
			andStr = " and ";
		}
		else if (voSearchCriteria.getConsultantsIsNotNull() && voSearchCriteria.getConsultants().size() > 0)
		{
			if (voSearchCriteria.getConsultants().get(0).getConsultantIsNotNull())
			{
				if (voSearchCriteria.getConsultants().get(0).getConsultantIsNotNull())
				{
					/*
					select i1_1.pasEvent.id, p1_1.consultant.id, m1_1.id, m1_1.mos.id, m2_1.id, m2_1.hcp.id, c1_1.consultant.id, m3_1.id, m3_1.mos.id, m4_1.id, m4_1.hcp.id
					from InpatientEpisode as i1_1 left join i1_1.pasEvent as p1_1 left join p1_1.consultant as m1_1 left join m1_1.mos as m2_1, ConsultantAccess as c1_1 left join c1_1.consultant as m3_1 left join m3_1.mos as m4_1
					where 
					(m2_1.hcp.id in (select m2_1.id
					from UserAccess as u1_1 left join u1_1.appUser as 
					a1_1 left join a1_1.mos as m1_1 left join 
					u1_1.consultantAccess as c1_1 left join 
					c1_1.consultant as m2_1 left join m2_1.mos as m3_1
					where
					(u1_1.appUser.id = 13210)))
					 and i1_1.isRIE is null  and c1_1.isRIE is null
					 */
					
					hql = "select i1_1 from InpatientEpisode as i1_1 left join i1_1.pasEvent as p1_1 left join p1_1.consultant as m1_1 left join m1_1.mos as m2_1, ConsultantAccess as c1_1 left join c1_1.consultant as m3_1 left join m3_1.mos as m4_1 " +
					"where m2_1.hcp.id in (";
					
					String strConsIds;
					StringBuffer strConsultantCodes;
					
					strConsultantCodes = new StringBuffer();
					
					for (int i=0; i < voSearchCriteria.getConsultants().size(); i++)
					{
						if (voSearchCriteria.getConsultants().get(i) != null)
						{
							strConsIds = voSearchCriteria.getConsultants().get(i).getConsultant().getID_Hcp().toString();
							if ( strConsIds != null)
							{ 
								if (i != 0) strConsultantCodes.append(",");								
								strConsultantCodes.append(strConsIds);	
							}
						}						
					}
					
					hql += strConsultantCodes + ")";
				}
			}
		}

		if (andStr.equals(" and "))
		{
			hql += " where ";
		}
		
		hql += condStr.toString();
		List ips = factory.find(hql, markers, values);
		
		ims.core.vo.InpatientEpisodeVoCollection inPatEpisodeColl =  InpatientEpisodeVoAssembler.createInpatientEpisodeVoCollectionFromInpatientEpisode(ips);		
		
		return populatePasContactFromInPatEpisode(inPatEpisodeColl).sort(SortOrder.DESCENDING);
	}

	private PasContactVoCollection populatePasContactFromInPatEpisode(InpatientEpisodeVoCollection voColl) 
	{
		PasContactVoCollection voCollPasContact = new PasContactVoCollection();
		
		for (int i = 0; i < voColl.size(); i++)
		{
			InpatientEpisodeVo vo = voColl.get(i);
			PasContactVo voPasContact = new PasContactVo();
		
			if (vo.getPasEventIsNotNull())
			{
				voPasContact.setAppointmentDateTime(vo.getPasEvent().getEventDateTime());
				voPasContact.setConsultant(vo.getPasEvent().getConsultant());
				voPasContact.setContactNumber(vo.getPasEvent().getID_PASEvent().toString());
				voPasContact.setContactType("Inpatient");
				voPasContact.setDischargeDateTime(vo.getPasEvent().getEventDateTime());
				voPasContact.setDocumentStatus(vo.getPasEvent().getCspDocumentStatus());
				voPasContact.setGPCode(vo.getPasEvent().getReferringGPIsNotNull()?vo.getPasEvent().getReferringGP().getNationalCode():null);
				voPasContact.setLocation(vo.getPasEvent().getLocation());
				voPasContact.setPatient(vo.getPasEvent().getPatient());
				voPasContact.setSpecialty(vo.getPasEvent().getSpecialty());
				voCollPasContact.add(voPasContact);
			}
		}
		return voCollPasContact;
	}

	private PasContactVoCollection listDTOInPatient(PatientListsFilterVo voSearchCriteria) throws DomainInterfaceException 
	{		
		PasContactVoCollection voPasContactColl= new PasContactVoCollection();		
		Inpatientlist inpat = (Inpatientlist)getDTOInstance(Inpatientlist.class);
		inpat.Filter.clear();
			
		if (voSearchCriteria.getDate() == null)
			throw new CodingRuntimeException("Discharge Date is a mandatory argument");

		inpat.Filter.Groupcond = buildUserAccessHQL(voSearchCriteria.getWard(), voSearchCriteria.getDate(), (voSearchCriteria.getDateEndIsNotNull() ? voSearchCriteria.getDateEnd() : null),null, voSearchCriteria.getConsultants(), voSearchCriteria.getSpecialties(), voSearchCriteria.getDocumentStatus(), Boolean.TRUE);		
		if (inpat.Filter.Groupcond == null) return null;
		
		Result result = null;	

		int maxContacts = ConfigFlag.DOM.CONTACT_SEARCH_MAX_SIZE.getValue();
		result=inpat.list(maxContacts);		
		
		if (result == null)
		{
			InpatientlistRecord dtoInpatient;
			PasContactVo voPasContact;
			for (int i = 0; i < inpat.DataCollection.count(); i++)
			{
				dtoInpatient = inpat.DataCollection.get(i);			
				voPasContact = new PasContactVo();
				populatePasContactFromDtoInpatient(voPasContact, dtoInpatient);				
				
				if ((voPasContact.getDocumentStatusIsNotNull() && voPasContact.getDocumentStatus().equals(DocumentStatus.NOLETTERREQUIRED) == false))
				{
					Integer nDocStatus = dtoInpatient.Documentstatus == null || dtoInpatient.Documentstatus == "" ? new Integer(0) : new Integer(dtoInpatient.Documentstatus);
					if (nDocStatus != null && nDocStatus.intValue() != 0)
					{				
						DocumentStatus lkpInPatDocumentStatus = (DocumentStatus)getLookupService().getLookupInstance(DocumentStatus.class, DocumentStatus.TYPE_ID,nDocStatus.intValue() ); 
						if (lkpInPatDocumentStatus != null)
							voPasContact.setDocumentStatus(lkpInPatDocumentStatus);
					}
					else
						voPasContact.setDocumentStatus(DocumentStatus.LETTERREQUIRED);
				}
				
				voPasContactColl.add(voPasContact);
			}
	
			return voPasContactColl.sort(SortOrder.DESCENDING);
		}
		else
		{
			 throw new DomainInterfaceException(result.toString());
		}
	}

	public UserAccessFullVo getFullUserAccessForUser(Integer appUserID) 
	{
		UserProfile impl = (UserProfile) getDomainImpl(UserProfileImpl.class);		
		return impl.getFullUserAccessForUser(appUserID);
	}

	public CorrespondenceDetailsVo getCorrespondenceDetailsByPasEvent(PASEventRefVo voPasEventRef) 
	{
		if (voPasEventRef == null)
			throw new CodingRuntimeException("Mandatory Argument - PAS Event Required");
		
		CorrespondenceDetails cspDet = (CorrespondenceDetails)getDomainImpl(CorrespondenceDetailsImpl.class);	
		return cspDet.getCorrespondenceDetailsByPasEvent(voPasEventRef);
	
	}

	public CorrespondenceDetailsVo getCorrespondenceDetails(CorrespondenceDetailsRefVo voCorrespRef) 
	{
		if (voCorrespRef == null)
			throw new CodingRuntimeException("Mandatory Argument - Correspondence Details Required");
		
		CorrespondenceDetails cspDet = (CorrespondenceDetails)getDomainImpl(CorrespondenceDetailsImpl.class);	
		return cspDet.getCorrespondenceDetails(voCorrespRef);
	}


	public PasEventVo getPasEvent(DateTime eventDateTime, PatientRefVo voPatienRef, String pasEventId) throws DomainInterfaceException
	{
		//ADT impl =  (ADT) getDomainImpl(ADTImpl.class);
		//return impl.getPasEventByUnqIdx(voPatienRef, pasEventId);
		if (pasEventId == null)
			throw new CodingRuntimeException("Mandatory Argument: IdPasEvent");

		DomainFactory factory = getDomainFactory();

		PASEvent pasEvent = (PASEvent) factory.getDomainObject(PASEvent.class,new Integer(pasEventId));
		return PasEventVoAssembler.create(pasEvent);
		
	}

	public PasEventVo createPasEvent(DateTime eventDateTime, PatientRefVo voRefPatient, PasContactVo voPasContact) throws StaleObjectException, UniqueKeyViolationException  
	{
		ADT impl =  (ADT) getDomainImpl(ADTImpl.class);
		return impl.createPasEvent(eventDateTime, voRefPatient, voPasContact);	
	}

	public PasEventWithReferringGPVo getPasEventwithReferringGP(PASEventRefVo pasEventRefVo)
	{
		return null; 
	}

	public PasEventWithReferringGPVo getPasEventWithGpPractices(DateTime eventDateTime, PatientRefVo voRefPatient, String str) throws DomainInterfaceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public DocumentVo getCurrentDocument(CorrespondenceDetailsRefVo voRefCorrespondenceDetail) 
	{
		if (voRefCorrespondenceDetail == null)
			throw new CodingRuntimeException("Mandatory Argument - Correspondence Details Required");
		
		CorrespondenceDetails cspDet = (CorrespondenceDetails)getDomainImpl(CorrespondenceDetailsImpl.class);	
		return cspDet.getCurrentDocument(voRefCorrespondenceDetail);
	}

	public PasEventVo getPasEvent(PASEventRefVo voPasEventRef) 
	{
		if (voPasEventRef == null)
			throw new CodingRuntimeException("Mandatory Argument - PAS Event Required");
		
		ADT impl = (ADT)getDomainImpl(ADTImpl.class);
		return impl.getPasEvent(voPasEventRef);		
	}

	public InpatientEpisodeVoCollection listInPatients(LocationRefVo refVoWard, MedicRefVo refVoMedic) 
	{		
		DomainFactory factory = getDomainFactory();
		String hql;
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();
		
		hql = " from InpatientEpisode ip "; 
		StringBuffer condStr = new StringBuffer();
		String andStr = " ";
		if (refVoWard != null)
		{
			condStr.append(andStr + " ip.pasEvent.location.id = :ward");
			markers.add("ward");
			values.add(refVoWard.getID_Location());
			andStr = " and ";
		}
		if (refVoMedic != null)
		{
			condStr.append(andStr + " ip.pasEvent.consultant.id = :cons");
			markers.add("cons");
			values.add(refVoMedic.getID_Hcp());
			andStr = " and ";
		}
	

		if (andStr.equals(" and "))
		{
			hql += " where ";
		}
		
		hql += condStr.toString();
		List ips = factory.find(hql, markers, values);
		
		return InpatientEpisodeVoAssembler.createInpatientEpisodeVoCollectionFromInpatientEpisode(ips);
	}

	public MedicLiteVoCollection listActiveMedics(String strMedicName) 
	{
		HcpAdmin hcpAdmin = (HcpAdmin)getDomainImpl(HcpAdminImpl.class);
		return hcpAdmin.listActiveMedics(strMedicName);		
	}

	public ConsultantAccessFullVoCollection listConsultantAccessForUser(Integer userId, String filterText) 
	{
		if (userId == null)
			throw new CodingRuntimeException("Mandatory Argument - Application User Id");

		DomainFactory factory = getDomainFactory();
		String hql = "select c1_1 from UserAccess as u1_1 join u1_1.consultantAccess as c1_1 join u1_1.appUser.mos as m1_1 where m1_1.hcp.id = :consId";
		StringBuffer condStr = new StringBuffer();
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();
		 
		markers.add("consId");
		values.add(userId);

		if (filterText != null && filterText.length() > 0)
		{
			String surnameSearch = filterText.toUpperCase();
			surnameSearch = surnameSearch.substring(0, Math.min(10, filterText.length()));
			hql = "select c1_1 from UserAccess as u1_1 join u1_1.consultantAccess as c1_1 left join u1_1.appUser.mos as m1_1 " +
					"join c1_1.consultant as c2_1 join c2_1.mos as m3_1 where ((m1_1.hcp.id = :consId) " +
					"and (m3_1.name.upperSurname like :mosSname))";
			markers.add("mosSname");
			values.add(surnameSearch + "%");
		}								
			
		hql += condStr.toString();
		List conAccess = factory.find(hql, markers, values);
			
		return ConsultantAccessFullVoAssembler.createConsultantAccessFullVoCollectionFromConsultantAccess(conAccess);
	}

	public ClinicAccessFullVoCollection listClinicAccessForUser(Integer idUser) 
	{
		if (idUser == null)
			throw new CodingRuntimeException("Mandatory Argument - Application User Id");

		DomainFactory factory = getDomainFactory();
		String hql = "select cl1_1 from UserAccess as u1_1 join u1_1.clinicAccess as cl1_1 left join u1_1.appUser.mos as m1_1 where m1_1.hcp.id = :consId";
		StringBuffer condStr = new StringBuffer();
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();
		 
		markers.add("consId");
		values.add(idUser);
			
		hql += condStr.toString();
		List clinicAccess = factory.find(hql, markers, values);
			
		return ClinicAccessFullVoAssembler.createClinicAccessFullVoCollectionFromClinicAccess(clinicAccess);
	}

	public SpecialtyAccessVoCollection listSpecialtyAccessForUser(Integer userId) 
	{
		if (userId == null)
			throw new CodingRuntimeException("Mandatory Argument - Application User Id");

		DomainFactory factory = getDomainFactory();
		String hql = "select s1_1 from UserAccess as u1_1 join u1_1.specialtyAccess as s1_1 left join u1_1.appUser.mos as m1_1 where m1_1.hcp.id = :consId";
		StringBuffer condStr = new StringBuffer();
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();
		 
		markers.add("consId");
		values.add(userId);
			
		hql += condStr.toString();
		List specAccess = factory.find(hql, markers, values);
			
		
		if (specAccess != null)
			return SpecialtyAccessVoAssembler.createSpecialtyAccessVoCollectionFromSpecialtyAccess(specAccess);
		else
			return null;

	}

}
