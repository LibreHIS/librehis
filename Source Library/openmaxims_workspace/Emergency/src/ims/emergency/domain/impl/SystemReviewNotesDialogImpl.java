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
// This code was generated by Bogdan Tofei using IMS Development Environment (version 1.80 build 4828.20158)
// Copyright (C) 1995-2013 IMS MAXIMS. All rights reserved.

package ims.emergency.domain.impl;

import java.util.List;

import ims.core.admin.vo.CareContextRefVo;
import ims.domain.DomainFactory;
import ims.emergency.domain.base.impl.BaseSystemReviewNotesDialogImpl;
import ims.emergency.domain.objects.SystemsReview;
import ims.emergency.vo.SystemReviewVoCollection;
import ims.emergency.vo.domain.SystemReviewVoAssembler;
import ims.framework.exceptions.CodingRuntimeException;

public class SystemReviewNotesDialogImpl extends BaseSystemReviewNotesDialogImpl
{

	private static final long serialVersionUID = 1L;

	public void saveSystemsReview(ims.emergency.vo.SystemReviewVoCollection systems) throws ims.domain.exceptions.StaleObjectException
	{
		if(systems == null)
			throw new CodingRuntimeException("SystemReviewVo not provided");
		
		DomainFactory factory = getDomainFactory();
	
		for (int i = 0; i < systems.size(); i++)
		{
			SystemsReview doSystemReview = SystemReviewVoAssembler.extractSystemsReview(factory, systems.get(i));
			factory.save(doSystemReview);
		}
	}

	public SystemReviewVoCollection getAllActiveSystemNotes(CareContextRefVo attendance)
	{
		DomainFactory factory = getDomainFactory();

		/* TODO MSSQL case - String hsql = "select sys from SystemsReview as sys left join sys.attendance as att where (att.id = :idcareContext) and (sys.isRIE is null or sys.isRIE = 0) and (sys.isCorrected is null or sys.isCorrected = 0)"; */
		String hsql = "select sys from SystemsReview as sys left join sys.attendance as att where (att.id = :idcareContext) and (sys.isRIE is null or sys.isRIE = FALSE) and (sys.isCorrected is null or sys.isCorrected = FALSE)";
		
		List systemrev = factory.find(hsql, new String[] {"idcareContext"}, new Object[] {attendance.getID_CareContext()});
		
		if(systemrev != null && systemrev.size() > 0)
			return SystemReviewVoAssembler.createSystemReviewVoCollectionFromSystemsReview(systemrev);
		
		return null;
	}
}
