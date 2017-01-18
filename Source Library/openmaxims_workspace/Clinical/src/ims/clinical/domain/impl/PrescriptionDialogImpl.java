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
// This code was generated by Cristian Belciug using IMS Development Environment (version 1.80 build 4342.23748)
// Copyright (C) 1995-2011 IMS MAXIMS. All rights reserved.

package ims.clinical.domain.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ims.clinical.domain.base.impl.BasePrescriptionDialogImpl;
import ims.core.vo.MedicationLiteVo;
import ims.core.vo.MedicationLiteVoCollection;
import ims.core.vo.lookups.MedicationFrequency;
import ims.core.vo.lookups.Specialty;
import ims.domain.DomainFactory;

public class PrescriptionDialogImpl extends BasePrescriptionDialogImpl
{
	private static final long serialVersionUID = 1L;

	public MedicationLiteVoCollection listMedications(String filter, String ignored, Specialty specialty, Boolean excludeControlledDrugs)
	{
		if(filter == null)
			return null;
		
		filter = filter.trim().toUpperCase();
		
		if(filter == null || filter.length() == 0)
			return null;
		
		DomainFactory factory = getDomainFactory();
        Connection conection = factory.getJdbcConnection();
        
        StringBuffer sql = new StringBuffer();

        /* TODO MSSQL case - sql.append("select a.id, a.medication, a.noofdayssu, l.id, l.text from shcl_medicationhotl s1 LEFT OUTER JOIN shcl_medicationhot2 s2 ON s1.id = s2.shcl_medicationhotl_hotlistite LEFT OUTER JOIN core_medication2 a ON s2.medication = a.id LEFT OUTER JOIN core_medication2_keywords k ON a.id = k.id LEFT OUTER JOIN applookup_instance l ON a.lkp_frequencyd = l.id where (k.keyword like ? or a.medication like ?) and a.isactive = 1 "); */
        sql.append("select a.id, a.medication, a.noofdayssu, l.id, l.text from shcl_medicationhotl s1 LEFT OUTER JOIN shcl_medicationhot2 s2 ON s1.id = s2.shcl_medicationhotl_hotlistite LEFT OUTER JOIN core_medication2 a ON s2.medication = a.id LEFT OUTER JOIN core_medication2_keywords k ON a.id = k.id LEFT OUTER JOIN applookup_instance l ON a.lkp_frequencyd = l.id where (k.keyword like ? or a.medication like ?) and a.isactive = true ");
        
        if(specialty != null)
        {
        	sql.append(" and s1.lkp_specialty = ? ");
        }
        
        if (ignored != null && ignored.trim().length() > 0)
		{
        	sql.append(" and a.id not in ("+ignored+")");
		}
        
        if (Boolean.TRUE.equals(excludeControlledDrugs))
        {
			/* TODO MSSQL case - sql.append(" and (a.iscontroll is null OR a.iscontroll = 0)"); */
        	sql.append(" and (a.iscontroll is null OR a.iscontroll = FALSE)");
        }
        
        sql.append(" order by upper(a.medication)");
        
        MedicationLiteVoCollection medicationColl = new MedicationLiteVoCollection();
        PreparedStatement ps;
        try 
        {
        	ps = conection.prepareCall(sql.toString());
        	ps.setString(1, filter + "%");
        	ps.setString(2, filter + "%");
        	
        	if(specialty != null)
        	{
        		ps.setInt(3, specialty.getID());
        	}
        	//ps.setMaxRows(10);
        
        	ResultSet rs = ps.executeQuery();
        
        	while( rs.next() )
        	{
        		MedicationLiteVo vo = new MedicationLiteVo();
        		vo.setID_Medication(Integer.valueOf(rs.getString(1)));
        		vo.setMedicationName(rs.getString(2));
        		vo.setNoOfDaysSupplyDefault(rs.getString(3) != null ? Integer.valueOf(rs.getString(3)) : null);
        		vo.setFrequencyDefault((rs.getString(4) != null && rs.getString(5) != null) ? new MedicationFrequency(Integer.valueOf(rs.getString(4)), rs.getString(5), true) : null);
        		
        		medicationColl.add(vo);
        	}
        }
        catch (SQLException e) 
        {
        	e.printStackTrace();
        }
        
        return medicationColl;
	}
}
