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
// This code was generated by George Cristian Josan using IMS Development Environment (version 1.80 build 4050.19540)
// Copyright (C) 1995-2011 IMS MAXIMS. All rights reserved.

package ims.clinicaladmin.domain.impl;

import ims.clinicaladmin.domain.base.impl.BaseSelectSerumMarkerImpl;
import ims.clinicaladmin.vo.domain.TumourSerumMarkersLiteVoAssembler;
import ims.clinicaladmin.vo.domain.TumourSerumMarkersVoAssembler;
import ims.domain.exceptions.DomainRuntimeException;
import ims.oncology.configuration.domain.objects.TumourSerumMarker;

public class SelectSerumMarkerImpl extends BaseSelectSerumMarkerImpl
{

	private static final long serialVersionUID = 1L;

	public ims.clinicaladmin.vo.TumourSerumMarkersLiteVoCollection listSerumMarkers()
	{
		/* TODO MSSQL case - String query = " from TumourSerumMarker as serum where serum.isActive = 1"; */
		String query = " from TumourSerumMarker as serum where serum.isActive = true";

		return TumourSerumMarkersLiteVoAssembler.createTumourSerumMarkersLiteVoCollectionFromTumourSerumMarker(getDomainFactory().find(query));
	}

	public ims.clinicaladmin.vo.TumourSerumMarkersVo getSerumMarker(ims.oncology.configuration.vo.TumourSerumMarkerRefVo serumMarker)
	{
		if (serumMarker == null || !serumMarker.getID_TumourSerumMarkerIsNotNull())
			return null;
		
		return TumourSerumMarkersVoAssembler.create((TumourSerumMarker) getDomainFactory().getDomainObject(TumourSerumMarker.class, serumMarker.getID_TumourSerumMarker()));
	}

	public ims.clinicaladmin.vo.TumourSerumMarkersVo saveSerumMarker(ims.clinicaladmin.vo.TumourSerumMarkersVo serumMarker) throws ims.domain.exceptions.StaleObjectException, ims.domain.exceptions.ForeignKeyViolationException, ims.domain.exceptions.UniqueKeyViolationException
	{
		// Check for value to save
		if (serumMarker == null)
			throw new DomainRuntimeException("Domain Error - Can not save a null Serum Marker record");
		
		// Check for validated record
		if (!serumMarker.isValidated())
			throw new DomainRuntimeException("Domain Error - Serum Marker record must be validated before save");
		
		// Get domain object
		TumourSerumMarker domSerumMarker = TumourSerumMarkersVoAssembler.extractTumourSerumMarker(getDomainFactory(), serumMarker);

		// Attempt save
		getDomainFactory().save(domSerumMarker);
		
		return TumourSerumMarkersVoAssembler.create(domSerumMarker);
	}
}
