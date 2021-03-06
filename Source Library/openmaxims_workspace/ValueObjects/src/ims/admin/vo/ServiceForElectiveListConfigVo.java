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
// This code was generated by Barbara Worwood using IMS Development Environment (version 1.80 build 5589.25814)
// Copyright (C) 1995-2015 IMS MAXIMS. All rights reserved.
// WARNING: DO NOT MODIFY the content of this file

package ims.admin.vo;

/**
 * Linked to core.clinical.Service business object (ID: 1003100032).
 */
public class ServiceForElectiveListConfigVo extends ims.core.vo.ServiceLiteVo implements ims.vo.ImsCloneable, Comparable
{
	private static final long serialVersionUID = 1L;

	public ServiceForElectiveListConfigVo()
	{
	}
	public ServiceForElectiveListConfigVo(Integer id, int version)
	{
		super(id, version);
	}
	public ServiceForElectiveListConfigVo(ims.admin.vo.beans.ServiceForElectiveListConfigVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.servicename = bean.getServiceName();
		this.servicecategory = bean.getServiceCategory() == null ? null : ims.core.vo.lookups.ServiceCategory.buildLookup(bean.getServiceCategory());
		this.canbescheduled = bean.getCanBeScheduled();
		this.specialty = bean.getSpecialty() == null ? null : ims.core.vo.lookups.Specialty.buildLookup(bean.getSpecialty());
		this.expectedturnaround = bean.getExpectedTurnaround();
		this.turnaroundmeasure = bean.getTurnaroundMeasure() == null ? null : ims.ocrr.vo.lookups.MinReorderPeriod.buildLookup(bean.getTurnaroundMeasure());
		this.maternityindicator = bean.getMaternityIndicator();
		this.isactive = bean.getIsActive();
		this.issubjecttorttclock = bean.getIsSubjectToRTTClock();
		this.canreferintotheservice = bean.getCanReferIntoTheService();
		this.directselfreferralsaccepted = bean.getDirectSelfReferralsAccepted();
		this.ispreassessmentcompletionexempt = bean.getIsPreAssessmentCompletionExempt();
		this.servicedescription = bean.getServiceDescription();
		this.uppername = bean.getUpperName();
	}
	public void populate(ims.vo.ValueObjectBeanMap map, ims.admin.vo.beans.ServiceForElectiveListConfigVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.servicename = bean.getServiceName();
		this.servicecategory = bean.getServiceCategory() == null ? null : ims.core.vo.lookups.ServiceCategory.buildLookup(bean.getServiceCategory());
		this.canbescheduled = bean.getCanBeScheduled();
		this.specialty = bean.getSpecialty() == null ? null : ims.core.vo.lookups.Specialty.buildLookup(bean.getSpecialty());
		this.expectedturnaround = bean.getExpectedTurnaround();
		this.turnaroundmeasure = bean.getTurnaroundMeasure() == null ? null : ims.ocrr.vo.lookups.MinReorderPeriod.buildLookup(bean.getTurnaroundMeasure());
		this.maternityindicator = bean.getMaternityIndicator();
		this.isactive = bean.getIsActive();
		this.issubjecttorttclock = bean.getIsSubjectToRTTClock();
		this.canreferintotheservice = bean.getCanReferIntoTheService();
		this.directselfreferralsaccepted = bean.getDirectSelfReferralsAccepted();
		this.ispreassessmentcompletionexempt = bean.getIsPreAssessmentCompletionExempt();
		this.servicedescription = bean.getServiceDescription();
		this.uppername = bean.getUpperName();
	}
	public ims.vo.ValueObjectBean getBean()
	{
		return this.getBean(new ims.vo.ValueObjectBeanMap());
	}
	public ims.vo.ValueObjectBean getBean(ims.vo.ValueObjectBeanMap map)
	{
		ims.admin.vo.beans.ServiceForElectiveListConfigVoBean bean = null;
		if(map != null)
			bean = (ims.admin.vo.beans.ServiceForElectiveListConfigVoBean)map.getValueObjectBean(this);
		if (bean == null)
		{
			bean = new ims.admin.vo.beans.ServiceForElectiveListConfigVoBean();
			map.addValueObjectBean(this, bean);
			bean.populate(map, this);
		}
		return bean;
	}
	public Object getFieldValueByFieldName(String fieldName)
	{
		if(fieldName == null)
			throw new ims.framework.exceptions.CodingRuntimeException("Invalid field name");
		fieldName = fieldName.toUpperCase();
		if(fieldName.equals("SERVICEDESCRIPTION"))
			return getServiceDescription();
		if(fieldName.equals("UPPERNAME"))
			return getUpperName();
		return super.getFieldValueByFieldName(fieldName);
	}
	public boolean getServiceDescriptionIsNotNull()
	{
		return this.servicedescription != null;
	}
	public String getServiceDescription()
	{
		return this.servicedescription;
	}
	public static int getServiceDescriptionMaxLength()
	{
		return 255;
	}
	public void setServiceDescription(String value)
	{
		this.isValidated = false;
		this.servicedescription = value;
	}
	public boolean getUpperNameIsNotNull()
	{
		return this.uppername != null;
	}
	public String getUpperName()
	{
		return this.uppername;
	}
	public static int getUpperNameMaxLength()
	{
		return 20;
	}
	public void setUpperName(String value)
	{
		this.isValidated = false;
		this.uppername = value;
	}
	public boolean isValidated()
	{
		if(this.isBusy)
			return true;
		this.isBusy = true;
	
		if(!this.isValidated)
		{
			this.isBusy = false;
			return false;
		}
		this.isBusy = false;
		return true;
	}
	public String[] validate()
	{
		return validate(null);
	}
	public String[] validate(String[] existingErrors)
	{
		if(this.isBusy)
			return null;
		this.isBusy = true;
	
		java.util.ArrayList<String> listOfErrors = new java.util.ArrayList<String>();
		if(existingErrors != null)
		{
			for(int x = 0; x < existingErrors.length; x++)
			{
				listOfErrors.add(existingErrors[x]);
			}
		}
		if(this.servicename == null || this.servicename.length() == 0)
			listOfErrors.add("Name is mandatory");
		else if(this.servicename.length() > 100)
			listOfErrors.add("The length of the field [servicename] in the value object [ims.admin.vo.ServiceForElectiveListConfigVo] is too big. It should be less or equal to 100");
		if(this.servicecategory == null)
			listOfErrors.add("Category is mandatory");
		if(this.servicedescription != null)
			if(this.servicedescription.length() > 255)
				listOfErrors.add("The length of the field [servicedescription] in the value object [ims.admin.vo.ServiceForElectiveListConfigVo] is too big. It should be less or equal to 255");
		if(this.uppername != null)
			if(this.uppername.length() > 20)
				listOfErrors.add("The length of the field [uppername] in the value object [ims.admin.vo.ServiceForElectiveListConfigVo] is too big. It should be less or equal to 20");
		int errorCount = listOfErrors.size();
		if(errorCount == 0)
		{
			this.isBusy = false;
			this.isValidated = true;
			return null;
		}
		String[] result = new String[errorCount];
		for(int x = 0; x < errorCount; x++)
			result[x] = (String)listOfErrors.get(x);
		this.isBusy = false;
		this.isValidated = false;
		return result;
	}
	public void clearIDAndVersion()
	{
		this.id = null;
		this.version = 0;
	}
	public Object clone()
	{
		if(this.isBusy)
			return this;
		this.isBusy = true;
	
		ServiceForElectiveListConfigVo clone = new ServiceForElectiveListConfigVo(this.id, this.version);
		
		clone.servicename = this.servicename;
		if(this.servicecategory == null)
			clone.servicecategory = null;
		else
			clone.servicecategory = (ims.core.vo.lookups.ServiceCategory)this.servicecategory.clone();
		clone.canbescheduled = this.canbescheduled;
		if(this.specialty == null)
			clone.specialty = null;
		else
			clone.specialty = (ims.core.vo.lookups.Specialty)this.specialty.clone();
		clone.expectedturnaround = this.expectedturnaround;
		if(this.turnaroundmeasure == null)
			clone.turnaroundmeasure = null;
		else
			clone.turnaroundmeasure = (ims.ocrr.vo.lookups.MinReorderPeriod)this.turnaroundmeasure.clone();
		clone.maternityindicator = this.maternityindicator;
		clone.isactive = this.isactive;
		clone.issubjecttorttclock = this.issubjecttorttclock;
		clone.canreferintotheservice = this.canreferintotheservice;
		clone.directselfreferralsaccepted = this.directselfreferralsaccepted;
		clone.ispreassessmentcompletionexempt = this.ispreassessmentcompletionexempt;
		clone.servicedescription = this.servicedescription;
		clone.uppername = this.uppername;
		clone.isValidated = this.isValidated;
		
		this.isBusy = false;
		return clone;
	}
	public int compareTo(Object obj)
	{
		return compareTo(obj, true);
	}
	public int compareTo(Object obj, boolean caseInsensitive)
	{
		if (obj == null)
		{
			return -1;
		}
		if(caseInsensitive); // this is to avoid eclipse warning only.
		if (!(ServiceForElectiveListConfigVo.class.isAssignableFrom(obj.getClass())))
		{
			throw new ClassCastException("A ServiceForElectiveListConfigVo object cannot be compared an Object of type " + obj.getClass().getName());
		}
		if (this.id == null)
			return 1;
		if (((ServiceForElectiveListConfigVo)obj).getBoId() == null)
			return -1;
		return this.id.compareTo(((ServiceForElectiveListConfigVo)obj).getBoId());
	}
	public synchronized static int generateValueObjectUniqueID()
	{
		return ims.vo.ValueObject.generateUniqueID();
	}
	public int countFieldsWithValue()
	{
		int count = super.countFieldsWithValue();
		if(this.servicedescription != null)
			count++;
		if(this.uppername != null)
			count++;
		return count;
	}
	public int countValueObjectFields()
	{
		return super.countValueObjectFields() + 2;
	}
	protected String servicedescription;
	protected String uppername;
	private boolean isValidated = false;
	private boolean isBusy = false;
}
