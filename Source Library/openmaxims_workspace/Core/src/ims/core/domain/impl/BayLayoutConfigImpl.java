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
// This code was generated by Marius Mihalec using IMS Development Environment (version 1.66 build 3243.27592)
// Copyright (C) 1995-2008 IMS MAXIMS plc. All rights reserved.

package ims.core.domain.impl;

import ims.core.admin.pas.domain.objects.WardBayConfig;
import ims.core.domain.BedInfoDialog;
import ims.core.domain.base.impl.BaseBayLayoutConfigImpl;
import ims.core.resource.place.vo.LocationRefVo;
import ims.core.vo.FloorBedSpaceLayoutLiteVoCollection;
import ims.core.vo.LocationLiteVoCollection;
import ims.core.vo.WardBayConfigForWardViewVo;
import ims.core.vo.WardBayConfigVo;
import ims.core.vo.domain.FloorBedSpaceLayoutLiteVoAssembler;
import ims.core.vo.domain.WardBayConfigForWardViewVoAssembler;
import ims.core.vo.domain.WardBayConfigVoAssembler;
import ims.core.vo.lookups.PreActiveActiveInactiveStatus;
import ims.domain.DomainFactory;
import ims.framework.exceptions.CodingRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class BayLayoutConfigImpl extends BaseBayLayoutConfigImpl
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public FloorBedSpaceLayoutLiteVoCollection listFloorBedLayouts() 
	{		
		DomainFactory factory = getDomainFactory();
		String hql = " from FloorBedSpaceLayout fl";
		
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();
		
		hql += " where fl.status.id = :status";
		markers.add("status");
		values.add(PreActiveActiveInactiveStatus.ACTIVE.getID());	
		
		return FloorBedSpaceLayoutLiteVoAssembler.createFloorBedSpaceLayoutLiteVoCollectionFromFloorBedSpaceLayout(factory.find(hql, markers, values));		
	}

	public LocationLiteVoCollection listActiveHospitalsLite()
	{
		BedInfoDialog impl = (BedInfoDialog) getDomainImpl(BedInfoDialogImpl.class);
		return impl.listActiveHospitalsLite();
	}

	public LocationLiteVoCollection listActiveWardsForHospitalLite(LocationRefVo hospital)
	{
		BedInfoDialog impl = (BedInfoDialog) getDomainImpl(BedInfoDialogImpl.class);
		return impl.listActiveWardsForHospitalLite(hospital);
	}

	public WardBayConfigVo getWardBayConfigByWard(LocationRefVo ward)
	{
		if (ward == null || ward.getID_Location() == null)
			throw new CodingRuntimeException("ward is null or id not provided in method getWardBayConfigByWard");
		
		DomainFactory factory = getDomainFactory();
		List<?> lstWbc = factory.find("from WardBayConfig wbc where wbc.ward.id = '" + ward.getID_Location() + "'");
		if(lstWbc != null && lstWbc.size() == 1)
			return WardBayConfigVoAssembler.create((WardBayConfig) lstWbc.get(0));
		
		return null;
	}

	public WardBayConfigForWardViewVo getWardBayConfigurationByWard(LocationRefVo ward) //WDEV-20328
	{
		if (ward == null || ward.getID_Location() == null)
			throw new CodingRuntimeException("ward is null or id not provided in method getWardBayConfigByWard");
		
		DomainFactory factory = getDomainFactory();
		List<?> lstWbc = factory.find("from WardBayConfig wbc where wbc.ward.id = '" + ward.getID_Location() + "'");
		if(lstWbc != null && lstWbc.size() == 1)
			return WardBayConfigForWardViewVoAssembler.create((WardBayConfig) lstWbc.get(0));
		
		return null;
	}
}
	
