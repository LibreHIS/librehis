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
// This code was generated by Rory Fitzpatrick using IMS Development Environment (version 1.66 build 3261.19720)
// Copyright (C) 1995-2008 IMS MAXIMS plc. All rights reserved.

package ims.core.domain.impl;

import ims.admin.domain.HcpAdmin;
import ims.admin.domain.OrganisationAndLocation;
import ims.admin.domain.impl.HcpAdminImpl;
import ims.admin.domain.impl.OrganisationAndLocationImpl;
import ims.configuration.gen.ConfigFlag;
import ims.core.admin.pas.domain.objects.HealthyLodger;
import ims.core.admin.pas.domain.objects.InpatientEpisode;
import ims.core.admin.pas.vo.InpatientEpisodeRefVo;
import ims.core.admin.pas.vo.PASEventRefVo;
import ims.core.domain.Demographics;
import ims.core.domain.WardView;
import ims.core.domain.base.impl.BaseCurrentInpatientListComponentImpl;
import ims.core.layout.vo.BedSpaceRefVo;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.place.domain.objects.Location;
import ims.core.resource.place.vo.LocationRefVo;
import ims.core.vo.BedSpaceStateLiteVo;
import ims.core.vo.CareContextShortVo;
import ims.core.vo.HcpFilter;
import ims.core.vo.HcpLiteVo;
import ims.core.vo.HcpLiteVoCollection;
import ims.core.vo.LocMostVo;
import ims.core.vo.LocationLiteVo;
import ims.core.vo.LocationLiteVoCollection;
import ims.core.vo.Patient;
import ims.core.vo.PatientShort;
import ims.core.vo.STHKCurrentInpatientListVo;
import ims.core.vo.STHKCurrentInpatientListVoCollection;
import ims.core.vo.domain.HealthyLodgerVoAssembler;
import ims.core.vo.domain.LocMostVoAssembler;
import ims.core.vo.domain.LocationLiteVoAssembler;
import ims.core.vo.domain.STHKCurrentInpatientListVoAssembler;
import ims.core.vo.lookups.PatIdType;
import ims.core.vo.lookups.TransferStatus;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.lookups.LookupInstance;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.interfaces.ILocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class STHKCurrentInpatientListComponentImpl extends BaseCurrentInpatientListComponentImpl
{

	private static final long serialVersionUID = 1L;

	public ims.core.vo.STHKCurrentInpatientListVoCollection listCurrentInpatients(ims.core.vo.CurrentInpatientListFilterVo voFilter)
	{
		if (voFilter == null)
			throw new CodingRuntimeException("Invalid voFilter");


		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Serializable> values = new ArrayList<Serializable>();
		boolean isCaseSensitivePatIdSearch = ConfigFlag.DOM.CASE_SENSITIVE_PATID.getValue();  //WDEV-18817

		StringBuffer sb = new StringBuffer();
		String andStr = "";

		String hql = ConfigFlag.UI.BED_INFO_UI_TYPE.getValue().equals("MAXIMS")  ? "select inpat, (select wcfg.wardStatus from WardBayConfig wcfg WHERE wcfg.ward.id = inpat.pasEvent.location.id),  (select hl from AdmissionDetail as adm left join adm.healthyLodger as hl where adm.pasEvent.id = inpat.pasEvent.id), (select count(pt.id) from PendingTransfers as pt left join pt.inpatientEpisode as ptinpat WHERE pt.currentStatus.id = :PENDING_TRANSF AND ptinpat.id = inpat.id) from BedSpaceState AS bstate RIGHT JOIN bstate.inpatientEpisode as inpat " : "select inpat from InpatientEpisode as inpat "; 

		String strSearchSurname = "";
		String strSearchForename = "";

		if (voFilter.getHospNumIsNotNull())
		{
			hql += " join inpat.pasEvent.patient.identifiers as ids ";
			
			String idVal = voFilter.getHospNum().trim();			
			if (voFilter.getIDType().equals(PatIdType.NHSN))
			{
				idVal = voFilter.getHospNum().replace(" ", "");//wdev-7305
			}
			if (!isCaseSensitivePatIdSearch) //WDEV-18817
			{	
				idVal = idVal.toUpperCase(); 
			}
			if (voFilter.getIDType().equals(PatIdType.NHSN))
			{
				sb.append(andStr + (!isCaseSensitivePatIdSearch ? " UPPER(ids.value)" : " ids.value") + " like :idnum"); //WDEV-18817 
				idVal += "%";
			}
			else
				sb.append(andStr + (!isCaseSensitivePatIdSearch ? " UPPER(ids.value)" : " ids.value") + " = :idnum");	//WDEV-18817 

			markers.add("idnum");
			andStr = " and ";
			sb.append(andStr + " ids.type = :idtype");
			markers.add("idtype");

			values.add(idVal);
			values.add(getDomLookup(voFilter.getIDType()));
		}
		else
		{
	
			if(voFilter.getForenameIsNotNull())
			{
				sb.append(andStr + " inpat.pasEvent.patient.name.upperForename like :patFore");
				markers.add("patFore");
				andStr = " and ";
	
				strSearchForename = voFilter.getForename().toUpperCase().trim();
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
	
			if(voFilter.getSurnameIsNotNull())
			{
				sb.append(andStr + " inpat.pasEvent.patient.name.upperSurname like :patSur");
				markers.add("patSur");
				andStr = " and ";
	
				strSearchSurname = voFilter.getSurname().toUpperCase().trim();
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
		
		if (voFilter.getAlertIsNotNull())
		{
			hql += " join inpat.pasEvent.patient.patientAlerts as patAlerts ";

			/* TODO MSSQL case - sb.append(andStr + "patAlerts.alertType = :alertID and patAlerts.isCurrentlyActiveAlert = 1"); */
			sb.append(andStr + "patAlerts.alertType = :alertID and patAlerts.isCurrentlyActiveAlert = true");

			markers.add("alertID");
			values.add(getDomLookup(voFilter.getAlert()));
			andStr = " and ";
		}

		if (voFilter.getConsultantIsNotNull())
		{
			sb.append(andStr + " inpat.pasEvent.consultant.id = :cons");
			markers.add("cons");
			values.add(voFilter.getConsultant().getID_Hcp());
			andStr = " and ";
		}
		//WDEV-20258
		if (voFilter.getHospitalIsNotNull() && voFilter.getWard() == null)
		{
			LocationLiteVoCollection wards = listWards(voFilter.getHospital().getID_Location(), null);
			if (wards.size() > 0)
			{	
				sb.append(andStr + " (inpat.pasEvent.location.id in " + getWardIds(wards));
				sb.append(" OR ");
			}
			sb.append((wards.size() > 0 ? "" : andStr) + " inpat.pasEvent.location.parentLocation.id = :hosp" + (wards.size() > 0 ? ")" : ""));
			markers.add("hosp");
			values.add(voFilter.getHospital().getID_Location());				
			andStr = " and ";
		}

		else if (voFilter.getWardIsNotNull())
		{
			sb.append(andStr + " inpat.pasEvent.location.id = :ward");
			markers.add("ward");
			values.add(voFilter.getWard().getID_Location());
			andStr = " and ";
		}
		//WDEV-20258 --- end of
		if (voFilter.getSideWardIsNotNull())
		{
			sb.append(andStr + " inpat.wardType = :sideW");
			markers.add("sideW");
			values.add(getDomLookup(voFilter.getSideWard()));
			andStr = " and ";
		}
	
		hql += " where ";
		hql += sb.toString();
		if (ConfigFlag.UI.BED_INFO_UI_TYPE.getValue().equals("MAXIMS")) //WDEV-20328
		{
			markers.add("PENDING_TRANSF");
			values.add(TransferStatus.PENDING.getId());
			
			List<?> results = getDomainFactory().find(hql.toString(), markers, values);
			STHKCurrentInpatientListVoCollection collResults = new STHKCurrentInpatientListVoCollection();
			if (results == null || results.isEmpty())
				return null;
			
			for (int i = 0; i<results.size();i++)
			{
				if (results.get(i) instanceof Object[])
				{
					Object[] ret = (Object[]) results.get(i);
					STHKCurrentInpatientListVo vo = new STHKCurrentInpatientListVo();
					if (ret[0] instanceof InpatientEpisode)
					{	
						//WDEV-22567
						InpatientEpisode doInpat = (InpatientEpisode) ret[0];
						if (doInpat != null && doInpat.getPasEvent() != null && doInpat.getPasEvent().getPatient() != null && doInpat.getPasEvent().getPatient().getAssociatedPatient() != null)
						{
							ims.core.patient.domain.objects.Patient mergedToPatient = doInpat.getPasEvent().getPatient().getAssociatedPatient();
							doInpat.getPasEvent().setPatient(mergedToPatient);
						}
						vo = STHKCurrentInpatientListVoAssembler.create(doInpat);
					}
					if (ret[1] instanceof LookupInstance)
					{	
						vo.setWardStatus(ims.core.vo.lookups.LookupHelper.getWardBayStatusInstance(getLookupService(), ((LookupInstance) ret[1]).getId()));
					}
					if (ret[2] instanceof HealthyLodger)
					{	
						vo.setHealthyLodgerDetails(HealthyLodgerVoAssembler.create((HealthyLodger) ret[2]));
					}
					if (ret[3] instanceof Long)
					{	
						vo.setHasPendingTransfer(((Long)ret[3]).intValue() > 0 ? Boolean.TRUE : Boolean.FALSE);
					}
					collResults.add(vo);					
				}					
			}
			return collResults;
		}
		return STHKCurrentInpatientListVoAssembler.createSTHKCurrentInpatientListVoCollectionFromInpatientEpisode(getDomainFactory().find(hql.toString(), markers, values));
	}

	public HcpLiteVo getHCP(Integer idHCP)
	{
		HcpAdmin implHcpAdmin = (HcpAdmin)getDomainImpl(HcpAdminImpl.class);
		HcpFilter voFilter = new HcpFilter();
		voFilter.setID_Hcp(idHCP);
		return implHcpAdmin.getHcpLite(voFilter);
	}
	//WDEV-20258
	private String getWardIds(LocationLiteVoCollection wards)
	{
		if (wards == null || wards.size() == 0)
			return "";
		
		StringBuilder idList = new StringBuilder();
		idList.append("(");
		for (int i=0; i<wards.size();i++)
		{
			if (wards.get(i) == null)
				continue;
			idList.append(wards.get(i).getID_Location().toString()).append(i == wards.size() - 1 ? ")": ", ");			
		}
		
		return idList.toString();
	}
	public LocationLiteVo getWard(LocationRefVo voWardRef) 
	{
		OrganisationAndLocation implLoc = (OrganisationAndLocation)getDomainImpl(OrganisationAndLocationImpl.class);
		return implLoc.getLocation(voWardRef.getID_Location());
	}

	public HcpLiteVoCollection listHCPs(HcpFilter filter)
	{
		HcpAdmin implHcpAdmin = (HcpAdmin)getDomainImpl(HcpAdminImpl.class);
		return implHcpAdmin.listHcpLite(filter);
	}

	public LocationLiteVoCollection listWards(Integer hospitalID, String searchName)
	{
		OrganisationAndLocation implLoc = (OrganisationAndLocation)getDomainImpl(OrganisationAndLocationImpl.class);
		LocationRefVo voRef = new LocationRefVo();
		voRef.setID_Location(hospitalID);
		return implLoc.listActiveWardsForHospitalByNameLite(voRef, searchName);
	}

	public LocationLiteVoCollection listActiveHospitalsLite()
	{
		OrganisationAndLocation impl = (OrganisationAndLocation) getDomainImpl(OrganisationAndLocationImpl.class);
		return impl.listActiveHospitalsLite();		
	}

	public PatientShort getPatientShort(PatientRefVo patientRefVo) 
	{
		Demographics impl = (Demographics) getDomainImpl(DemographicsImpl.class);
		Patient voPat = impl.getPatient(patientRefVo);		

		if (voPat != null)
			return (PatientShort)voPat;
		else
			return null;
	}

	public BedSpaceStateLiteVo getBedSpaceState(BedSpaceRefVo bed)
	{
		WardView impl = (WardView) getDomainImpl(WardViewImpl.class);
		return impl.getBedSpaceState(bed);		
	}

	public LocationLiteVo getHospital(LocationRefVo locationRefvo)
	{
		DomainFactory factory = getDomainFactory();
		return LocationLiteVoAssembler.create((Location) factory.getDomainObject(Location.class, locationRefvo.getID_Location()));
	}

	public STHKCurrentInpatientListVo getCurrentIPRecord(InpatientEpisodeRefVo ipRefVo) 
	{
		DomainFactory factory = getDomainFactory();
		return STHKCurrentInpatientListVoAssembler.create((InpatientEpisode) factory.getDomainObject(InpatientEpisode.class, ipRefVo.getID_InpatientEpisode()));
	}

	public STHKCurrentInpatientListVo saveIP(STHKCurrentInpatientListVo voIP) throws DomainInterfaceException, StaleObjectException 
	{
		if(voIP == null)
			throw new CodingRuntimeException("Cannot save null STHKCurrentInpatientListVo");
		
		if (!voIP.isValidated())
			throw new CodingRuntimeException("Inpatient Record has not been validated");
		
		DomainFactory factory = getDomainFactory();
		
		InpatientEpisode doIP = STHKCurrentInpatientListVoAssembler.extractInpatientEpisode(factory, voIP);
		
		//WDEV-18059 patient was discharged form another session.
		if (doIP == null)
			throw new StaleObjectException(doIP);
			
		factory.save(doIP);
		return (STHKCurrentInpatientListVoAssembler.create(doIP));		
	}

	public LocMostVo getLocation(LocationRefVo voLocRef)
	{
		DomainFactory factory = getDomainFactory();
		return LocMostVoAssembler.create((Location) factory.getDomainObject(Location.class, voLocRef.getID_Location()));
	}

	
	//WDEV-20707
	public LocationLiteVo getCurrentHospital(ILocation location) 
	{
		WardView impl = (WardView)getDomainImpl(WardViewImpl.class);
		return impl.getCurrentHospital(location);
	}
	
	public CareContextShortVo getCurrentCareContext(PASEventRefVo pasEventRef)
	{
		WardView impl = (WardView) getDomainImpl(WardViewImpl.class);
		return impl.getCareContextForPasEvent(pasEventRef);
		
	}
}
