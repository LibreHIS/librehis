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
// This code was generated by Bogdan Tofei using IMS Development Environment (version 1.80 build 5465.13953)
// Copyright (C) 1995-2015 IMS MAXIMS. All rights reserved.

package ims.RefMan.domain.impl;

import ims.admin.domain.OrganisationAndLocation;
import ims.admin.domain.impl.OrganisationAndLocationImpl;
import ims.admin.helper.Keywords;
import ims.RefMan.domain.base.impl.BaseAddEmergencyTheatreImpl;
import ims.RefMan.domain.objects.CatsReferral;
import ims.RefMan.vo.CatsReferralCurrentStatusVo;
import ims.RefMan.vo.CatsReferralCurrentStatusVoCollection;
import ims.RefMan.vo.CatsReferralRefVo;
import ims.RefMan.vo.domain.CatsReferralCurrentStatusVoAssembler;
import ims.core.clinical.domain.objects.Service;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.place.domain.objects.Location;
import ims.core.resource.place.vo.LocationRefVo;
import ims.core.vo.Hcp;
import ims.core.vo.LocationListVo;
import ims.core.vo.LocationLiteVoCollection;
import ims.core.vo.ProcedureLiteVoCollection;
import ims.core.vo.ServiceLiteVo;
import ims.core.vo.ServiceLiteVoCollection;
import ims.core.vo.domain.HcpAssembler;
import ims.core.vo.domain.LocationListVoAssembler;
import ims.core.vo.domain.LocationLiteVoAssembler;
import ims.core.vo.domain.ProcedureLiteVoAssembler;
import ims.core.vo.domain.ServiceLiteVoAssembler;
import ims.core.vo.lookups.LocationType;
import ims.core.vo.lookups.ServiceCategory;
import ims.core.vo.lookups.Specialty;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;
import ims.framework.exceptions.CodingRuntimeException;
import ims.scheduling.domain.objects.PendingEmergencyTheatre;
import ims.scheduling.vo.PendingEmergencyTheatreRefVo;
import ims.scheduling.vo.PendingEmergencyTheatreVo;
import ims.scheduling.vo.domain.PendingEmergencyTheatreVoAssembler;
import ims.scheduling.vo.lookups.PendingEmergencyTheatreStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddEmergencyTheatreImpl extends BaseAddEmergencyTheatreImpl
{
	private static final long serialVersionUID = 1L;

	public PendingEmergencyTheatreVo savePendingEmergencyTheatre(PendingEmergencyTheatreVo pendingEmergencyTheatre) throws ims.domain.exceptions.StaleObjectException
	{

		if (pendingEmergencyTheatre == null)
			throw new CodingRuntimeException("Cannot save null Pending Emergency Theatre record");
		
		DomainFactory factory = getDomainFactory();
		
		PendingEmergencyTheatre domainPendingEmergencyTheatre = PendingEmergencyTheatreVoAssembler.extractPendingEmergencyTheatre(factory, pendingEmergencyTheatre);
		
		factory.save(domainPendingEmergencyTheatre);
		
		return PendingEmergencyTheatreVoAssembler.create(domainPendingEmergencyTheatre);
	}

	public ProcedureLiteVoCollection listProcedures(String name, Specialty specialty) throws DomainInterfaceException
	{
		
		if (specialty == null)
			return listProcedures(name);
		else
			return listHotlistProcedures(name, specialty);
		
	}

	private ProcedureLiteVoCollection listHotlistProcedures(String name, Specialty specialty) throws DomainInterfaceException
	{

		DomainFactory factory = getDomainFactory();

		/* TODO MSSQL case - StringBuffer hql = new StringBuffer("select proc from ProcedureHotlist as procHotList left join procHotList.hotlistItem as procHotListItem left join procHotListItem.procedure as proc left join proc.keywords as kw " +
						   "where (proc.outpatientOnlyProcedure = 0 or proc.outpatientOnlyProcedure is null ) and (proc.medicalWL = 0 or proc.medicalWL is null )");  */
		StringBuffer hql = new StringBuffer("select proc from ProcedureHotlist as procHotList left join procHotList.hotlistItem as procHotListItem left join procHotListItem.procedure as proc left join proc.keywords as kw " +
						   "where (proc.outpatientOnlyProcedure = FALSE or proc.outpatientOnlyProcedure is null ) and (proc.medicalWL = FALSE or proc.medicalWL is null )");
		
		ArrayList names = new ArrayList();
		ArrayList values = new ArrayList();

		hql.append(" and proc.isActive = :isActive");
		names.add("isActive");
		values.add(Boolean.TRUE);	

		if (specialty !=null)
		{
			hql.append(" and procHotList.specialty = :spec");
			names.add("spec");
			values.add(getDomLookup(specialty));			
		}
		 
		return ProcedureLiteVoAssembler.createProcedureLiteVoCollectionFromProcedure(Keywords.searchByKeywords(factory, name, hql.toString(), names, values)).sort();
	}

	private ProcedureLiteVoCollection listProcedures(String name) throws DomainInterfaceException
	{
		DomainFactory factory = getDomainFactory();

		/* TODO MSSQL case - StringBuffer hql = new StringBuffer(" from Procedure proc join proc.keywords as kw where (proc.outpatientOnlyProcedure = 0 or proc.outpatientOnlyProcedure is null ) and (proc.medicalWL = 0 or proc.medicalWL is null ) "); */
		StringBuffer hql = new StringBuffer(" from Procedure proc join proc.keywords as kw where (proc.outpatientOnlyProcedure = FALSE or proc.outpatientOnlyProcedure is null ) and (proc.medicalWL = FALSE or proc.medicalWL is null ) ");
		
		ArrayList names = new ArrayList();
		ArrayList values = new ArrayList();
		
		hql.append(" and proc.isActive = :isActive");
		names.add("isActive");
		values.add(Boolean.TRUE);
		
		return ProcedureLiteVoAssembler.createProcedureLiteVoCollectionFromProcedure(Keywords.searchByKeywords(factory, name, hql.toString(), names, values)).sort();
	}

	public ServiceLiteVoCollection listServices(String name)
	{
		/* TODO MSSQL case - List<?> services = getDomainFactory().find("select srv from Service as srv left join srv.serviceCategory as servCateg where srv.isActive = 1 and servCateg.id = :serviceCategoryID and srv.upperName like :servName", new String[]{"servName", "serviceCategoryID"}, new Object[]{name.toUpperCase() + "%", ServiceCategory.CLINICAL.getID()}); */
		List<?> services = getDomainFactory().find("select srv from Service as srv left join srv.serviceCategory as servCateg where srv.isActive = true and servCateg.id = :serviceCategoryID and srv.upperName like :servName", new String[]{"servName", "serviceCategoryID"}, new Object[]{name.toUpperCase() + "%", ServiceCategory.CLINICAL.getID()});
		
		if (services == null || services.isEmpty())
			return null;
		
		return ServiceLiteVoAssembler.createServiceLiteVoCollectionFromService(services).sort();
	}

	public LocationListVo getLocation(LocationRefVo location)
	{
		if (location == null || location.getID_Location() == null)
			return null;
		
		return LocationListVoAssembler.create((Location) getDomainFactory().getDomainObject(Location.class, location.getID_Location()));
	}

	public LocationLiteVoCollection listActiveHospitals(String name)
	{
		DomainFactory factory = getDomainFactory();

		String hql = " from Location loc where loc.isActive = :active and loc.isVirtual = :isVirtual and loc.type = :locType ";

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Serializable> values = new ArrayList<Serializable>();
		
		markers.add("active");
		values.add(Boolean.TRUE);
		
		markers.add("isVirtual");
		values.add(Boolean.FALSE);
				
		markers.add("locType");
		values.add(getDomLookup(LocationType.HOSP));
		
		if (name != null)
		{
			hql += ("  and loc.upperName like :hospitalName");
			markers.add("hospitalName");
			values.add(name.toUpperCase() + "%");
		}
		
		hql += " ORDER BY loc.upperName";					
		
		return LocationLiteVoAssembler.createLocationLiteVoCollectionFromLocation(factory.find(hql, markers, values));
	}

	public LocationLiteVoCollection getWardsForHospital(LocationRefVo location, String name)
	{
		if (location == null)
			return null;
		
		OrganisationAndLocation impl = (OrganisationAndLocation) getDomainImpl(OrganisationAndLocationImpl.class);
		return impl.listActiveWardsForHospitalByNameLite(location, name);
	}

	public PendingEmergencyTheatreVo getPendingEmergencyTheatreForPatient(PatientRefVo patient, CatsReferralRefVo referral)
	{
		if (patient == null || referral == null)
			return null;
		
		DomainFactory factory = getDomainFactory();

		String hql = " select PET from PendingEmergencyTheatre as PET left join PET.patient as pat left join PET.catsReferral as referral left join PET.currentStatus as status " +
				     "where pat.id = :patID and referral.id = :referralID and status.id <> :statusID";

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Serializable> values = new ArrayList<Serializable>();
		
		markers.add("patID");
		values.add(patient.getID_Patient());
		
		markers.add("referralID");
		values.add(referral.getID_CatsReferral());
				
		markers.add("statusID");
		values.add(PendingEmergencyTheatreStatus.REMOVED.getID());
		
		List <?> list = factory.find(hql, markers, values);
		
		if (list == null || list.size() == 0)
			return null;
		
		return PendingEmergencyTheatreVoAssembler.create((PendingEmergencyTheatre)list.get(0));
	}

	public PendingEmergencyTheatreVo getPendingEmergencyTheatre(PendingEmergencyTheatreRefVo pendingEmergencyTheatre)
	{
		if (pendingEmergencyTheatre == null || pendingEmergencyTheatre.getID_PendingEmergencyTheatre() == null)
			return null;
		
		return PendingEmergencyTheatreVoAssembler.create((PendingEmergencyTheatre) getDomainFactory().getDomainObject(PendingEmergencyTheatre.class, pendingEmergencyTheatre.getID_PendingEmergencyTheatre()));
	}

	public ServiceLiteVo getReferralService(CatsReferralRefVo referral)
	{
		if (referral == null || referral.getID_CatsReferral() == null)
			return null;
		
		DomainFactory factory = getDomainFactory();

		String hql = " select serv from CatsReferral as referral left join referral.referralDetails as refDetails left join refDetails.service as serv " +
				     " where referral.id = :referralID";

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Serializable> values = new ArrayList<Serializable>();
		
		markers.add("referralID");
		values.add(referral.getID_CatsReferral());
		
		List <?> list = factory.find(hql, markers, values);
		
		if (list == null || list.size() == 0)
			return null;
		
		return ServiceLiteVoAssembler.create((Service)list.get(0));
	}

	public Hcp getResponsibleHcpForInpatient(PatientRefVo patient)
	{
		if (patient == null || patient.getID_Patient() == null)
			return null;
		
		DomainFactory factory = getDomainFactory();

		String hql = " select medic from InpatientEpisode as inpEp left join inpEp.pasEvent as pasEv left join pasEv.patient as pat left join pasEv.consultant as medic " +
				     " where pat.id = :patientID";

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Serializable> values = new ArrayList<Serializable>();
		
		markers.add("patientID");
		values.add(patient.getID_Patient());
		
		List <?> list = factory.find(hql, markers, values);
		
		if (list == null || list.size() == 0)
			return null;
		
		return HcpAssembler.create((ims.core.resource.people.domain.objects.Hcp)list.get(0));
	}
		
	//WDEV-22760
	public CatsReferralCurrentStatusVo getCatsReferral(CatsReferralRefVo catsReferral)
	{
		if(catsReferral == null || catsReferral.getID_CatsReferral() == null)
			return null;

		DomainFactory factory = getDomainFactory();
		String query = "select catsReferral,cons from CatsReferral as catsReferral left join catsReferral.referralDetails as refDetails left join refDetails.consultant as cons where catsReferral.id = :CatsReferralId ";

		List<?> doCatsReferral = factory.find(query, new String[] {"CatsReferralId"}, new Object[] {catsReferral.getID_CatsReferral()});
		CatsReferralCurrentStatusVoCollection voList = new CatsReferralCurrentStatusVoCollection();
		CatsReferralCurrentStatusVo voCats = null;
		if(doCatsReferral != null && doCatsReferral.size() > 0)
		{
			for (int i = 0; i<doCatsReferral.size();i++)
			{
				if (doCatsReferral.get(i) instanceof Object[])
				{
					Object[] ret = (Object[]) doCatsReferral.get(i);

					if (ret[0] instanceof CatsReferral)
					{						
						voCats = CatsReferralCurrentStatusVoAssembler.create((CatsReferral) ret[0]);
						if (ret[1] instanceof ims.core.resource.people.domain.objects.Hcp)
							voCats.setResponsibleHCP(HcpAssembler.create((ims.core.resource.people.domain.objects.Hcp) ret[1]));
					}
					voList.add(voCats);
				}

			}
			return voList.get(0);
		}
		return null;
	}
}
