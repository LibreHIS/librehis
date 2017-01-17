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
// This code was generated by Cristian Belciug using IMS Development Environment (version 1.80 build 5427.27524)
// Copyright (C) 1995-2014 IMS MAXIMS. All rights reserved.

package ims.scheduling.domain.impl;

import ims.admin.domain.ServiceConfiguration;
import ims.admin.domain.impl.ServiceConfigurationImpl;
import ims.core.clinical.vo.ServiceRefVo;
import ims.core.vo.ServiceFunctionLiteVoCollection;
import ims.core.vo.ServiceTriageActionVo;
import ims.core.vo.domain.ServiceFunctionLiteVoAssembler;
import ims.core.vo.domain.ServiceLiteVoAssembler;
import ims.domain.DomainFactory;
import ims.scheduling.domain.base.impl.BaseLinkedAppointmentDetailsComponentImpl;

import java.util.ArrayList;
import java.util.List;

public class LinkedAppointmentDetailsComponentImpl extends BaseLinkedAppointmentDetailsComponentImpl
{
	private static final long serialVersionUID = 1L;

	public ims.core.vo.ServiceLiteVoCollection listService(String name)
	{
		DomainFactory factory = getDomainFactory();

		/* TODO MSSQL case - StringBuilder hql = new StringBuilder("select s from Service s where s.isActive = 1 and s.canBeScheduled = 1 "); */
		StringBuilder hql = new StringBuilder("select s from Service s where s.isActive = true and s.canBeScheduled = true ");
		
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();

		if (name != null)
		{
			hql.append(" and s.upperName like :ServiceName ");
			markers.add("ServiceName");
			values.add(name.toUpperCase() + "%");
		}
		
		hql.append( " order by s.upperName asc" );
		List services = factory.find(hql.toString(), markers, values);
		
		return ServiceLiteVoAssembler.createServiceLiteVoCollectionFromService(services);
	}

	public ServiceFunctionLiteVoCollection listServiceFunctions(ServiceRefVo service)
	{
		if(service == null || service.getID_Service() == null)
			return null;
		
		DomainFactory factory = getDomainFactory();

		/* TODO MSSQL case - String hql = "select servfunc from ServiceFunction servfunc where servfunc.service.id = :serviceid and servfunc.isActive = 1 order by UPPER(servfunc.function.text) asc"; */
		String hql = "select servfunc from ServiceFunction servfunc where servfunc.service.id = :serviceid and servfunc.isActive = true order by UPPER(servfunc.function.text) asc";
	
		List servFuncList = factory.find(hql, new String[]{"serviceid"}, new Object[]{service.getID_Service()});
		return ServiceFunctionLiteVoAssembler.createServiceFunctionLiteVoCollectionFromServiceFunction(servFuncList);
	}

	public ServiceTriageActionVo getServiceTriageActions(ServiceRefVo service)
	{
		ServiceConfiguration impl = (ServiceConfiguration) getDomainImpl(ServiceConfigurationImpl.class);
		return impl.getServiceTriageAction(service);
	}
}
