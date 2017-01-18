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

import ims.RefMan.domain.base.impl.BasePendingEmergencyTheatreForWardImpl;
import ims.core.patient.domain.objects.Patient;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.place.vo.LocationRefVo;
import ims.core.vo.PatientShort;
import ims.core.vo.domain.PatientShortAssembler;
import ims.domain.DomainFactory;
import ims.framework.utils.DateTime;
import ims.framework.utils.Time;
import ims.scheduling.domain.objects.PendingEmergencyTheatre;
import ims.scheduling.vo.PendingEmergencyTheatreListVoCollection;
import ims.scheduling.vo.PendingEmergencyTheatreRefVo;
import ims.scheduling.vo.PendingEmergencyTheatreVo;
import ims.scheduling.vo.domain.PendingEmergencyTheatreListVoAssembler;
import ims.scheduling.vo.domain.PendingEmergencyTheatreVoAssembler;
import ims.scheduling.vo.lookups.PendingEmergencyTheatreStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PendingEmergencyTheatreForWardImpl extends BasePendingEmergencyTheatreForWardImpl
{

	private static final long serialVersionUID = 1L;

	public PendingEmergencyTheatreVo getPendingEmergencyTheatre(PendingEmergencyTheatreRefVo pendingEmergencyTheatre)
	{
		if (pendingEmergencyTheatre == null || pendingEmergencyTheatre.getID_PendingEmergencyTheatre() == null)
			return null;
		
		return PendingEmergencyTheatreVoAssembler.create((PendingEmergencyTheatre) getDomainFactory().getDomainObject(PendingEmergencyTheatre.class, pendingEmergencyTheatre.getID_PendingEmergencyTheatre()));
	}

	public PendingEmergencyTheatreListVoCollection listPendingEmergencyTheatreRecords(LocationRefVo ward)
	{		
		if (ward == null)
			return null;
		
		DomainFactory factory = getDomainFactory();

		String hql = "select PET from PendingEmergencyTheatre as PET left join PET.currentStatus as status left join PET.expectedWard as ward ";

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Serializable> values = new ArrayList<Serializable>();
		String andStr = " where (status.id <> :removedStatusID and status.id <> :admittedStatusID)";
		
		markers.add("removedStatusID");
		values.add(PendingEmergencyTheatreStatus.REMOVED.getID());
		
		markers.add("admittedStatusID");
		values.add(PendingEmergencyTheatreStatus.ADMITTED.getID());
		
		
		andStr += (" and ward.id = :expectedWardID ");
		markers.add("expectedWardID");
		values.add(ward.getID_Location());
		
		andStr += (" and PET.expectedDateTime between :CURRENTDATE_LOW and :CURRENTDATE_HIGH");
		markers.add("CURRENTDATE_LOW");
		markers.add("CURRENTDATE_HIGH");
		Date startDateTime = new ims.framework.utils.Date().getDate();
		Date endDateTime = new DateTime(new ims.framework.utils.Date(),  new Time(23, 59, 59)).getJavaDate();
		values.add(startDateTime);
		values.add(endDateTime);	
		
		andStr += (" order by PET.systemInformation.creationDateTime asc ");
			
		hql += andStr.toString();			
		
		return PendingEmergencyTheatreListVoAssembler.createPendingEmergencyTheatreListVoCollectionFromPendingEmergencyTheatre(factory.find(hql, markers, values));
	}

	public PatientShort getPatientShort(PatientRefVo patient)
	{
		if (patient == null || patient.getID_Patient() == null)
			return null;
		
		return PatientShortAssembler.create((Patient) getDomainFactory().getDomainObject(Patient.class, patient.getID_Patient()));
	}
}
