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
// This code was generated by Barbara Worwood using IMS Development Environment (version 1.80 build 4261.20360)
// Copyright (C) 1995-2011 IMS MAXIMS. All rights reserved.

package ims.ccosched.domain.impl;

import java.util.ArrayList;
import java.util.List;

import ims.ccosched.domain.base.impl.BasePatientDiaryImpl;
import ims.configuration.InitConfig;
import ims.core.vo.domain.PatientApptDiaryVoAssembler;
import ims.domain.DomainFactory;

public class PatientDiaryImpl extends BasePatientDiaryImpl
{

	private static final long serialVersionUID = 1L;

	public boolean dtoOnly()
	{
		return InitConfig.getConfigType().equals("DTO");
	}

	/**
	* Returns a list of patient appointments for the patient and date range specified
	*/
	public ims.core.vo.PatientApptDiaryVoCollection listPatientAppts(ims.core.patient.vo.PatientRefVo patient, ims.framework.utils.Date fromDate, ims.framework.utils.Date toDate, Boolean activeOnly)
	{
		DomainFactory factory = getDomainFactory();

		StringBuffer hql = new StringBuffer();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		hql.append(" from PatientApptDiary d where d.patient.id = :patient ");
		names.add("patient");
		values.add(patient.getID_Patient());
		
		if (activeOnly != null && activeOnly)
		{
			/* TODO MSSQL case - hql.append(" and d.active = 1 "); */
			hql.append(" and d.active = true ");
		}
		if (fromDate != null && toDate != null)
		{
			hql.append(" and d.apptDate between :fromDate and :toDate");
			names.add("fromDate");
			names.add("toDate");
			values.add(fromDate.getDate());
			values.add(toDate.getDate());
		}
		else if (fromDate != null && toDate == null)
		{
			hql.append(" and d.apptDate > :fromDate");
			names.add("fromDate");
			values.add(fromDate.getDate());
		}
		else if (fromDate == null && toDate != null)
		{
			hql.append(" and d.apptDate < :toDate");
			names.add("toDate");
			values.add(toDate.getDate());
		}
		
		hql.append(" order by d.apptDate");
		
		List l = factory.find(hql.toString(), names, values);
		if (l == null || l.size() == 0)
			return null;
		
		return PatientApptDiaryVoAssembler.createPatientApptDiaryVoCollectionFromPatientApptDiary(l);
	}
}
