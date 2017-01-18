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
// This code was generated by Bogdan Tofei using IMS Development Environment (version 1.80 build 5007.25751)
// Copyright (C) 1995-2013 IMS MAXIMS. All rights reserved.

package ims.core.domain.impl;

import ims.admin.domain.HcpAdmin;
import ims.admin.domain.OrganisationAndLocation;
import ims.admin.domain.impl.HcpAdminImpl;
import ims.admin.domain.impl.OrganisationAndLocationImpl;
import ims.RefMan.domain.objects.PatientElectiveList;
import ims.RefMan.domain.objects.TCIForPatientElectiveList;
import ims.RefMan.vo.PatientElectiveListForPendingAdmissionVo;
import ims.RefMan.vo.PatientElectiveListForPendingAdmissionVoCollection;
import ims.RefMan.vo.PatientElectiveListRefVo;
import ims.RefMan.vo.PatientElectiveTCIBedManagerCommentVo;
import ims.RefMan.vo.domain.PatientElectiveListForPendingAdmissionVoAssembler;
import ims.RefMan.vo.domain.PatientElectiveTCIBedManagerCommentVoAssembler;
import ims.configuration.gen.ConfigFlag;
import ims.core.domain.WardView;
import ims.core.domain.base.impl.BasePendingElectiveAdmissionsListELComponentImpl;
import ims.core.patient.domain.objects.Patient;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.place.domain.objects.Location;
import ims.core.resource.place.vo.LocationRefVo;
import ims.core.vo.HcpFilter;
import ims.core.vo.HcpLiteVo;
import ims.core.vo.HcpLiteVoCollection;
import ims.core.vo.LocMostVo;
import ims.core.vo.LocationLiteVo;
import ims.core.vo.PatientShort;
import ims.core.vo.PendingElectiveAdmissionListFilterVo;
import ims.core.vo.domain.LocMostVoAssembler;
import ims.core.vo.domain.LocationLiteVoAssembler;
import ims.core.vo.domain.PatientShortAssembler;
import ims.core.vo.lookups.PatIdType;
import ims.core.vo.lookups.WaitingListStatus;
import ims.domain.DomainFactory;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.lookups.LookupInstance;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.interfaces.ILocation;
import ims.framework.utils.DateTime;
import ims.framework.utils.Time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PendingElectiveAdmissionsListELComponentImpl extends BasePendingElectiveAdmissionsListELComponentImpl
{

	private static final long serialVersionUID = 1L;

	public HcpLiteVoCollection listHCPs(ims.core.vo.HcpFilter filter)
	{
		HcpAdmin implHcpAdmin = (HcpAdmin)getDomainImpl(HcpAdminImpl.class);
		return implHcpAdmin.listHcpLite(filter);
	}

	public ims.core.vo.LocationLiteVoCollection listWards(Integer hospitalID, String searchName)
	{
		OrganisationAndLocation implLoc = (OrganisationAndLocation)getDomainImpl(OrganisationAndLocationImpl.class);
		LocationRefVo voRef = new LocationRefVo();
		voRef.setID_Location(hospitalID);
		return implLoc.listActiveWardsForHospitalByNameLite(voRef, searchName);
	}

	public HcpLiteVo getHCP(Integer idHCP)
	{
		HcpAdmin implHcpAdmin = (HcpAdmin)getDomainImpl(HcpAdminImpl.class);
		HcpFilter voFilter = new HcpFilter();
		voFilter.setID_Hcp(idHCP);
		return implHcpAdmin.getHcpLite(voFilter);
	}

	public LocationLiteVo getWard(ims.core.resource.place.vo.LocationRefVo voWardRef)
	{
		OrganisationAndLocation implLoc = (OrganisationAndLocation)getDomainImpl(OrganisationAndLocationImpl.class);
		return implLoc.getLocation(voWardRef.getID_Location());
	}

	public ims.core.vo.LocationLiteVoCollection listActiveHospitalsLite()
	{
		OrganisationAndLocation impl = (OrganisationAndLocation) getDomainImpl(OrganisationAndLocationImpl.class);
		return impl.listActiveHospitalsLite();
	}

	public LocationLiteVo getHospital(ims.core.resource.place.vo.LocationRefVo locationRefvo)
	{
		DomainFactory factory = getDomainFactory();
		return LocationLiteVoAssembler.create((Location) factory.getDomainObject(Location.class, locationRefvo.getID_Location()));
	}

	public LocMostVo getLocation(ims.core.resource.place.vo.LocationRefVo voLocRef)
	{
		DomainFactory factory = getDomainFactory();
		return LocMostVoAssembler.create((Location) factory.getDomainObject(Location.class, voLocRef.getID_Location()));
	}

	public PatientElectiveListForPendingAdmissionVoCollection getElectiveLists(PendingElectiveAdmissionListFilterVo searchFilter)
	{
		if (searchFilter == null)
			throw new CodingRuntimeException("Invalid searchFilter");


		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Serializable> values = new ArrayList<Serializable>();
		boolean isCaseSensitivePatIdSearch = ConfigFlag.DOM.CASE_SENSITIVE_PATID.getValue();  //WDEV-18817

		StringBuffer sb = new StringBuffer();
		String andStr = "";

		String hql = "select pel, (select wcfg.wardStatus from WardBayConfig wcfg WHERE wcfg.ward.id = pel.tCIDetails.tCIWard.id) from PatientElectiveList as pel left join pel.patient as pat"; 
		
		String strSearchSurname = "";
		String strSearchForename = "";
		
		// Mandatory conditions for search
		sb.append(andStr + " pel.tCIDetails is not null AND pel.tCIDetails.tCIDate is not null AND pel.tCIDetails.isActive = :ACTIVE AND pel.tCIDetails.currentOutcome is null AND");
		markers.add("ACTIVE");
		values.add(Boolean.TRUE);

		/* TODO MSSQL case - sb.append (" (pel.isRIE is null OR pel.isRIE = 0) AND "); */
		sb.append (" (pel.isRIE is null OR pel.isRIE = FALSE) AND ");
		
		if (searchFilter.getHospNumIsNotNull())
		{
			hql += " left join pat.identifiers as ids ";
			
			String idVal = searchFilter.getHospNum().trim();			
			if (searchFilter.getIDType().equals(PatIdType.NHSN))
				idVal = searchFilter.getHospNum().replace(" ", "");
			if (!isCaseSensitivePatIdSearch)
			{	
				idVal = idVal.toUpperCase();
			}
			if (searchFilter.getIDType().equals(PatIdType.NHSN))
			{
				sb.append(andStr + (!isCaseSensitivePatIdSearch ? " UPPER(ids.value)" : " ids.value") + " like :idnum");
				idVal += "%";
			}
			else
				sb.append(andStr + (!isCaseSensitivePatIdSearch ? " UPPER(ids.value)" : " ids.value") + " = :idnum");

			markers.add("idnum");
			values.add(idVal);
			
			andStr = " and ";
			sb.append(andStr + " ids.type = :idtype");
			markers.add("idtype");
			values.add(getDomLookup(searchFilter.getIDType()));
			andStr = " and ";
		}
		else
		{
			if(searchFilter.getForenameIsNotNull())
			{
				sb.append(andStr + " pat.name.upperForename like :patFore");
				markers.add("patFore");
				andStr = " and ";
				
				strSearchForename = searchFilter.getForename().toUpperCase().trim();
				strSearchForename = strSearchForename.replaceAll("[^a-zA-Z]", "");
					
				if(strSearchForename.length() >= 40)
				{
					strSearchForename = strSearchForename.substring(0,40);
					strSearchForename += '%';
				}
				else
				{
					strSearchForename += '%';
				}
				
				values.add(strSearchForename);
	
			}
	
			if(searchFilter.getSurnameIsNotNull())
			{
				sb.append(andStr + " pat.name.upperSurname like :patSur");
				markers.add("patSur");
				andStr = " and ";
	
				strSearchSurname = searchFilter.getSurname().toUpperCase().trim();
				strSearchSurname = strSearchSurname.replaceAll("[^a-zA-Z]", "");
					
				if(strSearchSurname.length() >= 40)
				{
					strSearchSurname = strSearchSurname.substring(0,40);
					strSearchSurname += '%';
				}
				else
				{
					strSearchSurname += '%';
				}
				
				values.add(strSearchSurname);
			}
		}
		
		if (searchFilter.getAlertIsNotNull())
		{
			hql += " left join pat.patientAlerts as patAlerts ";

			sb.append(andStr + " patAlerts.alertType = :alertID");
			markers.add("alertID");
			values.add(getDomLookup(searchFilter.getAlert()));
			andStr = " and ";
		}

		if (searchFilter.getConsultantIsNotNull())
		{
			hql += " left join pel.responsibleHCP as respHCP "; //WDEV-22774
			sb.append(andStr + " respHCP.id = :cons");
			markers.add("cons");
			values.add(searchFilter.getConsultant().getID_Hcp());
			andStr = " and ";
		}
		
		if (searchFilter.getHospitalIsNotNull())
		{
			sb.append(andStr + " pel.tCIDetails.tCIHospital.id = :hosp");//WDEV-19348
			markers.add("hosp");
			values.add(searchFilter.getHospital().getID_Location());
			andStr = " and ";
		}
		
		if (searchFilter.getWardIsNotNull())
		{
			sb.append(andStr + " pel.tCIDetails.tCIWard.id = :ward");//WDEV-19348
			markers.add("ward");
			values.add(searchFilter.getWard().getID_Location());
			andStr = " and ";
		}
		
		if (searchFilter.getTCIIsNotNull())
		{ 
			if (Boolean.TRUE.equals(searchFilter.getTCIDateOnly()))
			{
				sb.append(andStr + " ( pel.tCIDetails.tCIDate >= :tci1 ");
				markers.add("tci1");
				values.add(new DateTime(searchFilter.getTCI(), new Time("00:00:00")).getJavaDate());
				andStr = " and ";
				sb.append(andStr + " pel.tCIDetails.tCIDate <= :tci2 )");
				markers.add("tci2");
				values.add(new DateTime(searchFilter.getTCI(), new Time("23:59:59")).getJavaDate());
				andStr = " and ";
			}
			else
			{
				sb.append(andStr + " pel.tCIDetails.tCIDate <= :tci");
				markers.add("tci");
				values.add(new DateTime(searchFilter.getTCI(), new Time("23:59:59")).getJavaDate());
				andStr = " and ";
			}
		}
		
		if (searchFilter.getSpecialtyIsNotNull())
		{
			hql += " left join pel.electiveList as el left join el.service as serv ";
			
			sb.append(andStr + " serv.specialty = :spec");
			markers.add("spec");
			values.add(getDomLookup(searchFilter.getSpecialty()));
			andStr = " and ";
		}
		
		if (searchFilter.getElectiveAdmissionTypeIsNotNull())
		{	
			sb.append(andStr + " pel.electiveAdmissionType.id = :ELECTIVE_TYPE");
			markers.add("ELECTIVE_TYPE");
			values.add(searchFilter.getElectiveAdmissionType().getID());
			andStr = " and ";
		}
		
		//WDEV-20809
		sb.append(andStr + " pel.electiveListStatus.electiveListStatus.id = :TCI_GIVEN");
		markers.add("TCI_GIVEN");
		values.add(WaitingListStatus.TCI_GIVEN.getID());
		
		hql += " where ";
		hql += sb.toString();
		
		//WDEV-20328 -- start
		List<?> results = getDomainFactory().find(hql.toString(), markers, values);

		if (results == null || results.isEmpty())
			return null;
		PatientElectiveListForPendingAdmissionVoCollection collResults = new PatientElectiveListForPendingAdmissionVoCollection();
		for (int i = 0; i<results.size();i++)
		{
			if (results.get(i) != null && results.get(i) instanceof Object[])
			{
				Object[] ret = (Object[]) results.get(i);
				PatientElectiveListForPendingAdmissionVo vo = new PatientElectiveListForPendingAdmissionVo();
				if (ret[0] instanceof PatientElectiveList)
				{	
					vo = PatientElectiveListForPendingAdmissionVoAssembler.create((PatientElectiveList) ret[0]);
					
					if (ret[1] instanceof LookupInstance)
					{	
						vo.setWardStatus(ims.core.vo.lookups.LookupHelper.getWardBayStatusInstance(getLookupService(), ((LookupInstance) ret[1]).getId()));
					}
					collResults.add(vo);	
				}							
			}					
		}
		return collResults;
		//WDEV-20328 --- ends here
		//return PatientElectiveListForPendingAdmissionVoAssembler.createPatientElectiveListForPendingAdmissionVoCollectionFromPatientElectiveList(getDomainFactory().find(hql.toString(), markers, values));
	}

	public PatientShort getPatientShort(PatientRefVo patientRef)
	{
		if(patientRef == null)
			throw new CodingRuntimeException("Cannot get Patient on null patientRef.");
		
		return PatientShortAssembler.create((Patient) getDomainFactory().getDomainObject(Patient.class, patientRef.getID_Patient()));
	}

	public PatientElectiveListForPendingAdmissionVo getCurrentPendingRecord(PatientElectiveListRefVo patientElectiveListRef)
	{
		if(patientElectiveListRef == null)
			throw new CodingRuntimeException("Cannot get PatientElectiveList on null patientElectiveListRef.");
		
		return PatientElectiveListForPendingAdmissionVoAssembler.create((PatientElectiveList) getDomainFactory().getDomainObject(PatientElectiveList.class, patientElectiveListRef.getID_PatientElectiveList()));
	}

	public void saveTCIDetails(PatientElectiveTCIBedManagerCommentVo tciDetails) throws StaleObjectException
	{
		if (tciDetails == null)
			throw new CodingRuntimeException("Cannot save null TCIDetails");

		DomainFactory factory = getDomainFactory();
		TCIForPatientElectiveList domainTCIDetails = PatientElectiveTCIBedManagerCommentVoAssembler.extractTCIForPatientElectiveList(factory, tciDetails);

		factory.save(domainTCIDetails);
	}


	//WDEV-20707
	public LocationLiteVo getCurrentHospital(ILocation location) 
	{
		WardView impl = (WardView)getDomainImpl(WardViewImpl.class);
		return impl.getCurrentHospital(location);
	}

	
}
