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
// This code was generated by Vasile Purdila using IMS Development Environment (version 1.80 build 5589.25814)
// Copyright (C) 1995-2015 IMS MAXIMS. All rights reserved.

package ims.core.domain.impl;

import java.util.ArrayList;
import java.util.List;

import ims.core.domain.base.impl.BasePdsPatientDetailsImpl;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.lookups.LookupInstance;
import ims.emergency.vo.lookups.School;
import ims.emergency.vo.lookups.SchoolCollection;
import ims.framework.utils.Image;
import ims.framework.utils.ImagePath;

public class PdsPatientDetailsImpl extends BasePdsPatientDetailsImpl
{

	private static final long serialVersionUID = 1L;

	public String getCCOAlias(String szHospnum) throws ims.domain.exceptions.DomainInterfaceException
	{
		// TODO: Add your code here and change the return value.
		return null;
	}

	public ims.core.vo.lookups.OccupationCollection getOccupations(String text)
	{
		// TODO: Add your code here and change the return value.
		return null;
	}

	public ims.emergency.vo.lookups.SchoolCollection getSchool(String text)
	{
		if (text == null || text.length() == 0)
			throw new DomainRuntimeException("Cannot search on null value.");
		
		ArrayList names = new ArrayList();
		ArrayList values = new ArrayList();

		/* TODO MSSQL case - StringBuffer taxonomyHql = new StringBuffer("select li from LookupInstance as li left join li.type as l where l.id = :lookupType and li.active = 1 and upper(li.text) like :lkptext order by upper(li.text) asc"); */
		StringBuffer taxonomyHql = new StringBuffer("select li from LookupInstance as li left join li.type as l where l.id = :lookupType and li.active = true and upper(li.text) like :lkptext order by upper(li.text) asc");
		
		names.add("lookupType");								
		names.add("lkptext");
		
		values.add(School.TYPE_ID);					
		values.add(text.toUpperCase() + "%");			
		
		List <LookupInstance> list = getDomainFactory().find(taxonomyHql.toString(), names, values);
		
		if(list == null || list.size() == 0)
			return null;
		
		SchoolCollection coll = new SchoolCollection();
		
		for(int i=0; i<list.size(); i++)
		{
			LookupInstance element=list.get(i);
			
			if(element == null)
				continue;
			
			Image img = null;
			Image regImage = element.getImage();
			if (regImage != null)
			{
				img = new ImagePath(regImage.getImageId(), regImage.getImagePath());
			}
			
			School valueObject = new School(element.getId(), element.getText(), element.isActive(), null, img, element.getColor(), element.getOrder());
			coll.add(valueObject);		
		}
		
		return coll.size() > 0 ? coll : null;
	}
}
