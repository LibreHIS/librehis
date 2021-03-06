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

package ims.emergency.vo;

/**
 * Linked to core.admin.pas.PendingEmergencyAdmission business object (ID: 1014100011).
 */
public class PendingEmergencyAdmissionForTrackingVo extends ims.core.admin.pas.vo.PendingEmergencyAdmissionRefVo implements ims.vo.ImsCloneable, Comparable
{
	private static final long serialVersionUID = 1L;

	public PendingEmergencyAdmissionForTrackingVo()
	{
	}
	public PendingEmergencyAdmissionForTrackingVo(Integer id, int version)
	{
		super(id, version);
	}
	public PendingEmergencyAdmissionForTrackingVo(ims.emergency.vo.beans.PendingEmergencyAdmissionForTrackingVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.admissionstatus = bean.getAdmissionStatus() == null ? null : ims.core.vo.lookups.EmergencyAdmissionStatus.buildLookup(bean.getAdmissionStatus());
	}
	public void populate(ims.vo.ValueObjectBeanMap map, ims.emergency.vo.beans.PendingEmergencyAdmissionForTrackingVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.admissionstatus = bean.getAdmissionStatus() == null ? null : ims.core.vo.lookups.EmergencyAdmissionStatus.buildLookup(bean.getAdmissionStatus());
	}
	public ims.vo.ValueObjectBean getBean()
	{
		return this.getBean(new ims.vo.ValueObjectBeanMap());
	}
	public ims.vo.ValueObjectBean getBean(ims.vo.ValueObjectBeanMap map)
	{
		ims.emergency.vo.beans.PendingEmergencyAdmissionForTrackingVoBean bean = null;
		if(map != null)
			bean = (ims.emergency.vo.beans.PendingEmergencyAdmissionForTrackingVoBean)map.getValueObjectBean(this);
		if (bean == null)
		{
			bean = new ims.emergency.vo.beans.PendingEmergencyAdmissionForTrackingVoBean();
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
		if(fieldName.equals("ADMISSIONSTATUS"))
			return getAdmissionStatus();
		return super.getFieldValueByFieldName(fieldName);
	}
	public boolean getAdmissionStatusIsNotNull()
	{
		return this.admissionstatus != null;
	}
	public ims.core.vo.lookups.EmergencyAdmissionStatus getAdmissionStatus()
	{
		return this.admissionstatus;
	}
	public void setAdmissionStatus(ims.core.vo.lookups.EmergencyAdmissionStatus value)
	{
		this.isValidated = false;
		this.admissionstatus = value;
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
	
		PendingEmergencyAdmissionForTrackingVo clone = new PendingEmergencyAdmissionForTrackingVo(this.id, this.version);
		
		if(this.admissionstatus == null)
			clone.admissionstatus = null;
		else
			clone.admissionstatus = (ims.core.vo.lookups.EmergencyAdmissionStatus)this.admissionstatus.clone();
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
		if (!(PendingEmergencyAdmissionForTrackingVo.class.isAssignableFrom(obj.getClass())))
		{
			throw new ClassCastException("A PendingEmergencyAdmissionForTrackingVo object cannot be compared an Object of type " + obj.getClass().getName());
		}
		if (this.id == null)
			return 1;
		if (((PendingEmergencyAdmissionForTrackingVo)obj).getBoId() == null)
			return -1;
		return this.id.compareTo(((PendingEmergencyAdmissionForTrackingVo)obj).getBoId());
	}
	public synchronized static int generateValueObjectUniqueID()
	{
		return ims.vo.ValueObject.generateUniqueID();
	}
	public int countFieldsWithValue()
	{
		int count = 0;
		if(this.admissionstatus != null)
			count++;
		return count;
	}
	public int countValueObjectFields()
	{
		return 1;
	}
	protected ims.core.vo.lookups.EmergencyAdmissionStatus admissionstatus;
	private boolean isValidated = false;
	private boolean isBusy = false;
}
