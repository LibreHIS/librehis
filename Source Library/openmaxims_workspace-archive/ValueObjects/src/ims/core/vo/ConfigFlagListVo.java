//#############################################################################
//#                                                                           #
//#  Copyright (C) <2014>  <IMS MAXIMS>                                       #
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
//#############################################################################
//#EOH
// This code was generated by Barbara Worwood using IMS Development Environment (version 1.80 build 5007.25751)
// Copyright (C) 1995-2014 IMS MAXIMS. All rights reserved.
// WARNING: DO NOT MODIFY the content of this file

package ims.core.vo;

/**
 * Linked to core.configuration.ConfigFlag business object (ID: 1028100052).
 */
public class ConfigFlagListVo extends ims.core.configuration.vo.ConfigFlagRefVo implements ims.vo.ImsCloneable, Comparable
{
	private static final long serialVersionUID = 1L;

	public ConfigFlagListVo()
	{
	}
	public ConfigFlagListVo(Integer id, int version)
	{
		super(id, version);
	}
	public ConfigFlagListVo(ims.core.vo.beans.ConfigFlagListVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.name = bean.getName();
		this.value = bean.getValue();
		this.issystem = bean.getIsSystem();
		this.systeminformation = bean.getSystemInformation() == null ? null : bean.getSystemInformation().buildSystemInformation();
	}
	public void populate(ims.vo.ValueObjectBeanMap map, ims.core.vo.beans.ConfigFlagListVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.name = bean.getName();
		this.value = bean.getValue();
		this.issystem = bean.getIsSystem();
		this.systeminformation = bean.getSystemInformation() == null ? null : bean.getSystemInformation().buildSystemInformation();
	}
	public ims.vo.ValueObjectBean getBean()
	{
		return this.getBean(new ims.vo.ValueObjectBeanMap());
	}
	public ims.vo.ValueObjectBean getBean(ims.vo.ValueObjectBeanMap map)
	{
		ims.core.vo.beans.ConfigFlagListVoBean bean = null;
		if(map != null)
			bean = (ims.core.vo.beans.ConfigFlagListVoBean)map.getValueObjectBean(this);
		if (bean == null)
		{
			bean = new ims.core.vo.beans.ConfigFlagListVoBean();
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
		if(fieldName.equals("NAME"))
			return getName();
		if(fieldName.equals("VALUE"))
			return getValue();
		if(fieldName.equals("ISSYSTEM"))
			return getIsSystem();
		if(fieldName.equals("SYSTEMINFORMATION"))
			return getSystemInformation();
		return super.getFieldValueByFieldName(fieldName);
	}
	public boolean getNameIsNotNull()
	{
		return this.name != null;
	}
	public String getName()
	{
		return this.name;
	}
	public static int getNameMaxLength()
	{
		return 255;
	}
	public void setName(String value)
	{
		this.isValidated = false;
		this.name = value;
	}
	public boolean getValueIsNotNull()
	{
		return this.value != null;
	}
	public String getValue()
	{
		return this.value;
	}
	public static int getValueMaxLength()
	{
		return 2000;
	}
	public void setValue(String value)
	{
		this.isValidated = false;
		this.value = value;
	}
	public boolean getIsSystemIsNotNull()
	{
		return this.issystem != null;
	}
	public Boolean getIsSystem()
	{
		return this.issystem;
	}
	public void setIsSystem(Boolean value)
	{
		this.isValidated = false;
		this.issystem = value;
	}
	public boolean getSystemInformationIsNotNull()
	{
		return this.systeminformation != null;
	}
	public ims.vo.SystemInformation getSystemInformation()
	{
		return this.systeminformation;
	}
	public void setSystemInformation(ims.vo.SystemInformation value)
	{
		this.isValidated = false;
		this.systeminformation = value;
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
		if(this.name == null || this.name.length() == 0)
			listOfErrors.add("Name is mandatory");
		else if(this.name.length() > 255)
			listOfErrors.add("The length of the field [name] in the value object [ims.core.vo.ConfigFlagListVo] is too big. It should be less or equal to 255");
		if(this.value != null)
			if(this.value.length() > 2000)
				listOfErrors.add("The length of the field [value] in the value object [ims.core.vo.ConfigFlagListVo] is too big. It should be less or equal to 2000");
		if(this.issystem == null)
			listOfErrors.add("IsSystem is mandatory");
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
	
		ConfigFlagListVo clone = new ConfigFlagListVo(this.id, this.version);
		
		clone.name = this.name;
		clone.value = this.value;
		clone.issystem = this.issystem;
		if(this.systeminformation == null)
			clone.systeminformation = null;
		else
			clone.systeminformation = (ims.vo.SystemInformation)this.systeminformation.clone();
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
		if (!(ConfigFlagListVo.class.isAssignableFrom(obj.getClass())))
		{
			throw new ClassCastException("A ConfigFlagListVo object cannot be compared an Object of type " + obj.getClass().getName());
		}
		ConfigFlagListVo compareObj = (ConfigFlagListVo)obj;
		int retVal = 0;
		if (retVal == 0)
		{
			if(this.getValue() == null && compareObj.getValue() != null)
				return -1;
			if(this.getValue() != null && compareObj.getValue() == null)
				return 1;
			if(this.getValue() != null && compareObj.getValue() != null)
			{
				if(caseInsensitive)
					retVal = this.getValue().toLowerCase().compareTo(compareObj.getValue().toLowerCase());
				else
					retVal = this.getValue().compareTo(compareObj.getValue());
			}
		}
		return retVal;
	}
	public synchronized static int generateValueObjectUniqueID()
	{
		return ims.vo.ValueObject.generateUniqueID();
	}
	public int countFieldsWithValue()
	{
		int count = 0;
		if(this.name != null)
			count++;
		if(this.value != null)
			count++;
		if(this.issystem != null)
			count++;
		if(this.systeminformation != null)
			count++;
		return count;
	}
	public int countValueObjectFields()
	{
		return 4;
	}
	protected String name;
	protected String value;
	protected Boolean issystem;
	protected ims.vo.SystemInformation systeminformation;
	private boolean isValidated = false;
	private boolean isBusy = false;
}