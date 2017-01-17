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
// This code was generated by Rory Fitzpatrick using IMS Development Environment (version 1.80 build 4091.21781)
// Copyright (C) 1995-2011 IMS MAXIMS. All rights reserved.

package ims.oncology.domain.impl;

import ims.core.patient.vo.PatientRefVo;
import ims.core.vo.OutPatientAttendanceCCOChemoVoCollection;
import ims.core.vo.PatientApptDiaryVoCollection;
import ims.core.vo.domain.OutPatientAttendanceCCOChemoVoAssembler;
import ims.core.vo.domain.PatientApptDiaryVoAssembler;
import ims.domain.DomainFactory;
import ims.framework.utils.Date;
import ims.oncology.domain.base.impl.BasePatientsOutpatientAppointmentsImpl;

import java.util.ArrayList;
import java.util.List;

public class PatientsOutpatientAppointmentsImpl extends BasePatientsOutpatientAppointmentsImpl
{

	private static final long serialVersionUID = 1L;

	public OutPatientAttendanceCCOChemoVoCollection getPatientOPAttendences(ims.core.patient.vo.PatientRefVo patietnRefVo, ims.framework.utils.Date fromDate, ims.framework.utils.Date toDate, Integer pkey)
	{
		DomainFactory factory = getDomainFactory();
		String hql;
		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		
		hql = " from OutpatientAttendance op "; 
		StringBuffer condStr = new StringBuffer();
		String andStr = " and ";
		
		condStr.append(" op.isActive = :isActive");
		markers.add("isActive");
		values.add(Boolean.TRUE);
	
		if(fromDate != null)
		{
			condStr.append(andStr + " op.appointmentDateTime >= :dateFrom");
 			markers.add("dateFrom");
			values.add(fromDate.getDate());
			andStr = " and ";	
		}
		if(toDate != null)
		{
			condStr.append(andStr + " op.appointmentDateTime < :dateTo");
 			markers.add("dateTo");
			values.add(toDate.copy().addDay(1).getDate());
			andStr = " and ";	
		}

		condStr.append(andStr + " op.pasEvent.patient.id = :patID");
		markers.add("patID");
		values.add(patietnRefVo.getID_Patient());
		andStr = " and ";	
		
		hql += " where ";
		
		hql += condStr.toString();
		List ops = factory.find(hql, markers, values);
		
		return OutPatientAttendanceCCOChemoVoAssembler.createOutPatientAttendanceCCOChemoVoCollectionFromOutpatientAttendance(ops).sort();
	}

	public PatientApptDiaryVoCollection listPatientDiaryByPatient(PatientRefVo patient, Date fromDate, Date toDate) 
	{
		DomainFactory factory = getDomainFactory();
		String hql;
		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		/* TODO MSSQL case - hql = "select pd from PatientApptDiary pd where active = 1 and "; */
		hql = "select pd from PatientApptDiary pd where active = TRUE and ";

		StringBuffer condStr = new StringBuffer();
		String andStr = " and ";
		
		condStr.append(" pd.patient.id = :patID");
		markers.add("patID");
		values.add(patient.getID_Patient());


		if(fromDate != null)
		{
			condStr.append(andStr + " pd.apptDate >= :dateFrom");
 			markers.add("dateFrom");
			values.add(fromDate.getDate());
		}
		if(toDate != null)
		{
			condStr.append(andStr + " pd.apptDate < :dateTo");
 			markers.add("dateTo");
			values.add(toDate.copy().addDay(1).getDate());
		}

		andStr = " and ";	
		
		hql += condStr.toString();
		List ops = factory.find(hql, markers, values);
	
//		if (ops.size() > 0)
//		{
//			String hql2;
//			boolean bListClinics = false;
//			StringBuffer clinicIDS = new StringBuffer();
//			clinicIDS.append("select clin from Clinic clin left join clin.codeMappings as tax left join tax.taxonomyName as taxTyp where taxTyp = :taxonomyType and tax.taxonomyCode in (");
//			for (int i = 0 ; i < ops.size() ; i++)
//			{
//				PatientEvent domPE = (PatientEvent)ops.get(i);
//				if (domPE.getClinicCode() != null
//					&& domPE.getClinicCode() != "")
//				{
//					clinicIDS.append("'" + domPE.getClinicCode() + "',");
//					bListClinics = true;
//				}
//			}
//
//			if (clinicIDS.toString().endsWith(","))
//				hql2 = clinicIDS.toString().substring(0, clinicIDS.length() - 1);
//			else
//				hql2 = clinicIDS.toString();
//
//			hql2 += " )";
//
//			ClinicVoCollection voClinics = null;
//			if (bListClinics)
//				voClinics = ClinicVoAssembler.createClinicVoCollectionFromClinic(factory.find(hql2,new String[]{"taxonomyType"},new Object[]{getDomLookup(TaxonomyType.PAS)}));
//			
//			for (int x = 0 ; voClinics != null && x < voClinics.size() ; x++)
//			{
//				for (int y = 0 ; y < ops.size() ; y++)
//				{
//					PatientEvent domPE = (PatientEvent)ops.get(y);
//					if (domPE.getClinicCode()!= null
//						&& voClinics.get(x).getMapping(TaxonomyType.PAS) != null
//						&& domPE.getClinicCode().equals(voClinics.get(x).getMapping(TaxonomyType.PAS).toString()))
//						domPE.setClinicCode(voClinics.get(x).getClinicName());
//				}
//			}
//		}
		return PatientApptDiaryVoAssembler.createPatientApptDiaryVoCollectionFromPatientApptDiary(ops).sort();
	}

}
