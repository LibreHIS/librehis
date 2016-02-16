//#############################################################################
//#                                                                           #
//#  Copyright (C) <2014>  <IMS MAXIMS>                                       #
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
//#############################################################################
//#EOH
// This code was generated by Cornel Ventuneac using IMS Development Environment (version 1.80 build 4738.16294)
// Copyright (C) 1995-2013 IMS MAXIMS. All rights reserved.

package ims.admin.domain.impl;

import java.util.ArrayList;
import java.util.List;

import ims.admin.domain.base.impl.BaseExternalResourcesConfigImpl;
import ims.core.resource.domain.objects.ExternalResource;
import ims.core.vo.domain.ExternalResourceVoAssembler;
import ims.domain.DomainFactory;
import ims.framework.exceptions.CodingRuntimeException;


public class ExternalResourcesConfigImpl extends BaseExternalResourcesConfigImpl
{

	private static final long serialVersionUID = 1L;

	public ims.core.vo.ExternalResourceVo saveExternalResource(ims.core.vo.ExternalResourceVo record) throws ims.domain.exceptions.StaleObjectException, ims.domain.exceptions.UniqueKeyViolationException
	{
		if( record == null)
			throw new CodingRuntimeException("This ExternalResourceVo is null");
		
		DomainFactory factory = getDomainFactory();	
		ExternalResource doExternalResource = ExternalResourceVoAssembler.extractExternalResource(factory, record);
		factory.save(doExternalResource);
		return ExternalResourceVoAssembler.create(doExternalResource);
	}

	public ims.core.vo.ExternalResourceVoCollection getExternalResources(ims.core.vo.lookups.ExternalResourceType externalResourceType, String name, ims.core.vo.lookups.HcpDisType hcptype, ims.core.vo.lookups.PreActiveActiveInactiveStatus status)
	{
		ArrayList names = new ArrayList();
		ArrayList values = new ArrayList();
		String prepend = " where ";
		DomainFactory factory = getDomainFactory();
		StringBuffer hql = new StringBuffer("from ExternalResource as e1_1 ");
		if (externalResourceType != null)
		{
			hql.append(prepend + " e1_1.externalResourceType.id = :resourceType ");
			names.add("resourceType");
			values.add(externalResourceType.getID());
			prepend = " and ";
		}
		if (name != null)
		{
			hql.append(prepend + " upper(e1_1.resourcename) like :name");
			names.add("name");
			values.add(name.toUpperCase()+"%");
			prepend = " and ";
		}
		
		if (hcptype != null)
		{
			hql.append(prepend + " e1_1.hCPType.id = :idHcpType ");
			names.add("idHcpType");
			values.add(hcptype.getID());
			prepend = " and ";
		}
		if (status != null)
		{
			hql.append(prepend + " e1_1.resourceStatus.id = :idStatus ");
			names.add("idStatus");
			values.add(status.getID());
		
		}
		hql.append(" order by e1_1.systemInformation.creationDateTime asc");
		List extresources = factory.find(hql.toString(), names, values);
		return ExternalResourceVoAssembler.createExternalResourceVoCollectionFromExternalResource(extresources);
	}
	
	public ims.core.vo.ExternalResourceVo getExternalResourceById(ims.core.resource.vo.ExternalResourceRefVo externalResourceRef)
	{
		if( externalResourceRef == null)
			throw new CodingRuntimeException("This ExternalResourceRefVo is null");
		DomainFactory factory = getDomainFactory();
		ExternalResource doExternalResource = (ExternalResource) factory.getDomainObject(ExternalResource.class, externalResourceRef.getID_ExternalResource());
			
		return ExternalResourceVoAssembler.create(doExternalResource);
	}
}