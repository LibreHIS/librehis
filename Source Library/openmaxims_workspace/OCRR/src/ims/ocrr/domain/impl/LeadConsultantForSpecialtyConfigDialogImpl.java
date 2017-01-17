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
// This code was generated by Florin Blindu using IMS Development Environment (version 1.80 build 4342.23748)
// Copyright (C) 1995-2011 IMS MAXIMS. All rights reserved.

package ims.ocrr.domain.impl;

import ims.core.vo.domain.HcpLiteVoAssembler;
import ims.core.vo.lookups.Specialty;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.exceptions.UniqueKeyViolationException;
import ims.domain.exceptions.UnqViolationUncheckedException;
import ims.framework.exceptions.CodingRuntimeException;
import ims.ocrr.configuration.domain.objects.SpecialtyLeadConsultant;
import ims.ocrr.domain.base.impl.BaseLeadConsultantForSpecialtyConfigDialogImpl;
import ims.ocrr.vo.LeadConsultantForSpecialtyConfigVo;
import ims.ocrr.vo.domain.LeadConsultantForSpecialtyConfigVoAssembler;

import java.util.ArrayList;
import java.util.List;

public class LeadConsultantForSpecialtyConfigDialogImpl extends BaseLeadConsultantForSpecialtyConfigDialogImpl
{
	private static final long serialVersionUID = 1L;

	public ims.core.vo.HcpLiteVoCollection listHcpMedic(String hcpName)
	{
		DomainFactory factory = getDomainFactory();
		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		
		String hcpNameLite = hcpName != null ? hcpName.toUpperCase() + "%" : "%%";
		
		if (hcpName == null || hcpName.trim().length() == 0)
			throw new DomainRuntimeException("String for search is null.");

		StringBuffer hql = new StringBuffer();
			
		hql.append(" select hcp from Hcp as hcp left join hcp.mos as mos, Medic as medic");

		/* TODO MSSQL case - hql.append(" where mos.name.upperSurname like :HcpName and hcp.isActive = 1 and medic.isHCPaResponsibleHCP = 1 and medic.id = hcp.id "); */
		hql.append(" where mos.name.upperSurname like :HcpName and hcp.isActive = TRUE and medic.isHCPaResponsibleHCP = TRUE and medic.id = hcp.id ");

		hql.append(" order by mos.name.upperSurname asc");
		
		markers.add("HcpName");
		values.add(hcpNameLite);
		
		List<?> fact=factory.find(hql.toString(), markers, values);
		return HcpLiteVoAssembler.createHcpLiteVoCollectionFromHcp(fact);
	}

	public LeadConsultantForSpecialtyConfigVo saveLeadConsultant(LeadConsultantForSpecialtyConfigVo leadConsultant) throws StaleObjectException, ims.domain.exceptions.UniqueKeyViolationException
	{
		if (leadConsultant == null )

			throw new CodingRuntimeException("Cannot save leadConsultant if null or get LeadConsultant on null ID . ");
			
		if (!leadConsultant.isValidated())
			throw new CodingRuntimeException("LeadConsultantForSpecialtyConfigFBVo not Validated");
		
		try
		{
			DomainFactory factory = getDomainFactory();
			SpecialtyLeadConsultant hcpMedic = LeadConsultantForSpecialtyConfigVoAssembler.extractSpecialtyLeadConsultant(factory, leadConsultant);	
			
			if (getLeadConsultantBySpecialty(leadConsultant.getSpecialty()) && leadConsultant.getID_SpecialtyLeadConsultant()==null)
			{
				throw new StaleObjectException(hcpMedic);
			}
			
			factory.save(hcpMedic);
			
			return LeadConsultantForSpecialtyConfigVoAssembler.create(hcpMedic);
			
		}
		catch (UnqViolationUncheckedException e)
		{
			throw new UniqueKeyViolationException("This Lead Consultant already exist for this specialty!", e);
		}
	}

	public Boolean isStale(LeadConsultantForSpecialtyConfigVo globalContext)
	{
		if (globalContext == null || globalContext.getID_SpecialtyLeadConsultant()== null)
		{
			throw new CodingRuntimeException("Cannot get LeadConsultantForSpecialtyConfigFBVo on null Id ");
		}
				
		DomainFactory factory = getDomainFactory();
		SpecialtyLeadConsultant domainLeadCons = (SpecialtyLeadConsultant) factory.getDomainObject(SpecialtyLeadConsultant.class, globalContext.getID_SpecialtyLeadConsultant());
		
		if(domainLeadCons==null)
		{
			return true;
		}
		
		if (domainLeadCons.getVersion() > globalContext.getVersion_SpecialtyLeadConsultant())
		{
			return true;
		}
		
		return false;
	}

	public Boolean getLeadConsultantBySpecialty(Specialty specialtyLookups) 
	{
		if (specialtyLookups == null)
		{
			throw new CodingRuntimeException("Cannot get Specialty on null Id ");
		}

		DomainFactory factory = getDomainFactory();
		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
	
		StringBuffer hql = new StringBuffer();
		hql.append(" select spec.specialty.id from SpecialtyLeadConsultant as spec left join spec.specialty as lookspec ");
		hql.append(" where upper(lookspec.text) like :SpecLook  ");
		markers.add("SpecLook");
		values.add(specialtyLookups.getText());
	
		List<?> getSpec=factory.find(hql.toString(), markers, values);
		
		if (getSpec !=null && getSpec.size()>0)
			return true;
	
		return false;
	}
}
