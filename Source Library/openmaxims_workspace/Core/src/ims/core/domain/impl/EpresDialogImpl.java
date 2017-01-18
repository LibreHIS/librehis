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
// This code was generated by Sean Nesbitt using IMS Development Environment (version 1.80 build 4261.20360)
// Copyright (C) 1995-2011 IMS MAXIMS. All rights reserved.

package ims.core.domain.impl;

import java.util.List;

import ims.core.admin.domain.objects.CareContext;
import ims.core.admin.vo.CareContextRefVo;
import ims.core.domain.VitalSignsMetrics;
import ims.core.domain.base.impl.BaseEpresDialogImpl;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.people.domain.objects.MemberOfStaff;
import ims.core.vitals.domain.objects.Metrics;
import ims.core.vo.CareContextShortVo;
import ims.core.vo.MemberOfStaffMappingsLiteVo;
import ims.core.vo.TaxonomyMap;
import ims.core.vo.VSMetrics;
import ims.core.vo.VitalSignsVo;
import ims.core.vo.domain.CareContextShortVoAssembler;
import ims.core.vo.domain.MemberOfStaffMappingsLiteVoAssembler;
import ims.core.vo.domain.VSMetricsAssembler;
import ims.core.vo.lookups.TaxonomyType;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;

public class EpresDialogImpl extends BaseEpresDialogImpl
{

	private static final long serialVersionUID = 1L;

	public String getPASUser(Integer mosId) throws DomainInterfaceException
	{				
		DomainFactory factory = getDomainFactory();		
		MemberOfStaff mosDo = (MemberOfStaff)factory.getDomainObject(MemberOfStaff.class, mosId.intValue());
		if (mosDo==null)
			throw new DomainInterfaceException("User has no PAS mapping. Please add external mapping for PAS in User Administration.");		
		MemberOfStaffMappingsLiteVo mVo = MemberOfStaffMappingsLiteVoAssembler.create(mosDo);

		for (int i = 0; i < mVo.getCodeMappings().size(); i++) {
			TaxonomyMap element = mVo.getCodeMappings().get(i);
			if (element.getTaxonomyName().equals(TaxonomyType.PAS))
				return element.getTaxonomyCode();			
		}
		
		return "";
	}

	public VitalSignsVo getLastMetrics(CareContextRefVo refCareContext) throws DomainInterfaceException
	{
		VitalSignsMetrics vitalImpl = (VitalSignsMetrics)getDomainImpl(VitalSignsImpl.class);
		return vitalImpl.getLastMetrics(refCareContext);
	}

	public CareContextShortVo getCareContextMin(CareContextRefVo refCareContext) 
	{
		if(refCareContext == null)
			return null;
		DomainFactory factory = getDomainFactory();
		CareContext doCareContext = (CareContext)factory.getDomainObject(CareContext.class, refCareContext.getID_CareContext());
		return CareContextShortVoAssembler.create(doCareContext);
	}

	public VSMetrics getLatestMetricsForPatient(PatientRefVo patient) throws DomainInterfaceException {
		if (patient == null || patient.getID_Patient() == null)
			return null;

		DomainFactory factory = getDomainFactory();
		List metrics = factory.find("from Metrics metric where metric.patient.id = :idPatient " +
				/* TODO MSSQL case - " and (rie is null or rie = 0) order by metric.systemInformation.creationDateTime desc",new String[] {"idPatient"},new Object[] {patient.getID_Patient()}); */
				" and (rie is null or rie = FALSE) order by metric.systemInformation.creationDateTime desc",new String[] {"idPatient"},new Object[] {patient.getID_Patient()});

		if (metrics != null && metrics.size() > 0)
			return (VSMetricsAssembler.create((Metrics)metrics.get(0)));
		
		return null;
	}
}
