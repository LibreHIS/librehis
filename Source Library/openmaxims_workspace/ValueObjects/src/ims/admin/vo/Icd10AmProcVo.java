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

package ims.admin.vo;

/**
 * Linked to core.clinical.coding.Icd10AmProc business object (ID: 1037100001).
 */
public class Icd10AmProcVo extends ims.core.clinical.coding.vo.Icd10AmProcRefVo implements ims.vo.ImsCloneable, Comparable
{
	private static final long serialVersionUID = 1L;

	public Icd10AmProcVo()
	{
	}
	public Icd10AmProcVo(Integer id, int version)
	{
		super(id, version);
	}
	public Icd10AmProcVo(ims.admin.vo.beans.Icd10AmProcVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.code_id = bean.getCode_id();
		this.block = bean.getBlock() == null ? null : bean.getBlock().buildVo();
		this.ascii_desc = bean.getAscii_desc();
		this.ascii_short_desc = bean.getAscii_short_desc();
		this.effective_from = bean.getEffective_from() == null ? null : bean.getEffective_from().buildDate();
		this.inactive = bean.getInactive() == null ? null : bean.getInactive().buildDate();
		this.sex = bean.getSex();
		this.stype = bean.getStype();
		this.agel = bean.getAgeL();
		this.agelh = bean.getAgelH();
		this.atype = bean.getAtype();
	}
	public void populate(ims.vo.ValueObjectBeanMap map, ims.admin.vo.beans.Icd10AmProcVoBean bean)
	{
		this.id = bean.getId();
		this.version = bean.getVersion();
		this.code_id = bean.getCode_id();
		this.block = bean.getBlock() == null ? null : bean.getBlock().buildVo(map);
		this.ascii_desc = bean.getAscii_desc();
		this.ascii_short_desc = bean.getAscii_short_desc();
		this.effective_from = bean.getEffective_from() == null ? null : bean.getEffective_from().buildDate();
		this.inactive = bean.getInactive() == null ? null : bean.getInactive().buildDate();
		this.sex = bean.getSex();
		this.stype = bean.getStype();
		this.agel = bean.getAgeL();
		this.agelh = bean.getAgelH();
		this.atype = bean.getAtype();
	}
	public ims.vo.ValueObjectBean getBean()
	{
		return this.getBean(new ims.vo.ValueObjectBeanMap());
	}
	public ims.vo.ValueObjectBean getBean(ims.vo.ValueObjectBeanMap map)
	{
		ims.admin.vo.beans.Icd10AmProcVoBean bean = null;
		if(map != null)
			bean = (ims.admin.vo.beans.Icd10AmProcVoBean)map.getValueObjectBean(this);
		if (bean == null)
		{
			bean = new ims.admin.vo.beans.Icd10AmProcVoBean();
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
		if(fieldName.equals("CODE_ID"))
			return getCode_id();
		if(fieldName.equals("BLOCK"))
			return getBlock();
		if(fieldName.equals("ASCII_DESC"))
			return getAscii_desc();
		if(fieldName.equals("ASCII_SHORT_DESC"))
			return getAscii_short_desc();
		if(fieldName.equals("EFFECTIVE_FROM"))
			return getEffective_from();
		if(fieldName.equals("INACTIVE"))
			return getInactive();
		if(fieldName.equals("SEX"))
			return getSex();
		if(fieldName.equals("STYPE"))
			return getStype();
		if(fieldName.equals("AGEL"))
			return getAgeL();
		if(fieldName.equals("AGELH"))
			return getAgelH();
		if(fieldName.equals("ATYPE"))
			return getAtype();
		return super.getFieldValueByFieldName(fieldName);
	}
	public boolean getCode_idIsNotNull()
	{
		return this.code_id != null;
	}
	public String getCode_id()
	{
		return this.code_id;
	}
	public static int getCode_idMaxLength()
	{
		return 10;
	}
	public void setCode_id(String value)
	{
		this.isValidated = false;
		this.code_id = value;
	}
	public boolean getBlockIsNotNull()
	{
		return this.block != null;
	}
	public ims.admin.vo.Icd10AmBlockVo getBlock()
	{
		return this.block;
	}
	public void setBlock(ims.admin.vo.Icd10AmBlockVo value)
	{
		this.isValidated = false;
		this.block = value;
	}
	public boolean getAscii_descIsNotNull()
	{
		return this.ascii_desc != null;
	}
	public String getAscii_desc()
	{
		return this.ascii_desc;
	}
	public static int getAscii_descMaxLength()
	{
		return 255;
	}
	public void setAscii_desc(String value)
	{
		this.isValidated = false;
		this.ascii_desc = value;
	}
	public boolean getAscii_short_descIsNotNull()
	{
		return this.ascii_short_desc != null;
	}
	public String getAscii_short_desc()
	{
		return this.ascii_short_desc;
	}
	public static int getAscii_short_descMaxLength()
	{
		return 60;
	}
	public void setAscii_short_desc(String value)
	{
		this.isValidated = false;
		this.ascii_short_desc = value;
	}
	public boolean getEffective_fromIsNotNull()
	{
		return this.effective_from != null;
	}
	public ims.framework.utils.Date getEffective_from()
	{
		return this.effective_from;
	}
	public void setEffective_from(ims.framework.utils.Date value)
	{
		this.isValidated = false;
		this.effective_from = value;
	}
	public boolean getInactiveIsNotNull()
	{
		return this.inactive != null;
	}
	public ims.framework.utils.Date getInactive()
	{
		return this.inactive;
	}
	public void setInactive(ims.framework.utils.Date value)
	{
		this.isValidated = false;
		this.inactive = value;
	}
	public boolean getSexIsNotNull()
	{
		return this.sex != null;
	}
	public Integer getSex()
	{
		return this.sex;
	}
	public void setSex(Integer value)
	{
		this.isValidated = false;
		this.sex = value;
	}
	public boolean getStypeIsNotNull()
	{
		return this.stype != null;
	}
	public Integer getStype()
	{
		return this.stype;
	}
	public void setStype(Integer value)
	{
		this.isValidated = false;
		this.stype = value;
	}
	public boolean getAgeLIsNotNull()
	{
		return this.agel != null;
	}
	public Integer getAgeL()
	{
		return this.agel;
	}
	public void setAgeL(Integer value)
	{
		this.isValidated = false;
		this.agel = value;
	}
	public boolean getAgelHIsNotNull()
	{
		return this.agelh != null;
	}
	public Integer getAgelH()
	{
		return this.agelh;
	}
	public void setAgelH(Integer value)
	{
		this.isValidated = false;
		this.agelh = value;
	}
	public boolean getAtypeIsNotNull()
	{
		return this.atype != null;
	}
	public Integer getAtype()
	{
		return this.atype;
	}
	public void setAtype(Integer value)
	{
		this.isValidated = false;
		this.atype = value;
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
		if(this.block != null)
		{
			if(!this.block.isValidated())
			{
				this.isBusy = false;
				return false;
			}
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
		if(this.code_id != null)
			if(this.code_id.length() > 10)
				listOfErrors.add("The length of the field [code_id] in the value object [ims.admin.vo.Icd10AmProcVo] is too big. It should be less or equal to 10");
		if(this.block != null)
		{
			String[] listOfOtherErrors = this.block.validate();
			if(listOfOtherErrors != null)
			{
				for(int x = 0; x < listOfOtherErrors.length; x++)
				{
					listOfErrors.add(listOfOtherErrors[x]);
				}
			}
		}
		if(this.ascii_desc != null)
			if(this.ascii_desc.length() > 255)
				listOfErrors.add("The length of the field [ascii_desc] in the value object [ims.admin.vo.Icd10AmProcVo] is too big. It should be less or equal to 255");
		if(this.ascii_short_desc != null)
			if(this.ascii_short_desc.length() > 60)
				listOfErrors.add("The length of the field [ascii_short_desc] in the value object [ims.admin.vo.Icd10AmProcVo] is too big. It should be less or equal to 60");
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
	
		Icd10AmProcVo clone = new Icd10AmProcVo(this.id, this.version);
		
		clone.code_id = this.code_id;
		if(this.block == null)
			clone.block = null;
		else
			clone.block = (ims.admin.vo.Icd10AmBlockVo)this.block.clone();
		clone.ascii_desc = this.ascii_desc;
		clone.ascii_short_desc = this.ascii_short_desc;
		if(this.effective_from == null)
			clone.effective_from = null;
		else
			clone.effective_from = (ims.framework.utils.Date)this.effective_from.clone();
		if(this.inactive == null)
			clone.inactive = null;
		else
			clone.inactive = (ims.framework.utils.Date)this.inactive.clone();
		clone.sex = this.sex;
		clone.stype = this.stype;
		clone.agel = this.agel;
		clone.agelh = this.agelh;
		clone.atype = this.atype;
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
		if (!(Icd10AmProcVo.class.isAssignableFrom(obj.getClass())))
		{
			throw new ClassCastException("A Icd10AmProcVo object cannot be compared an Object of type " + obj.getClass().getName());
		}
		Icd10AmProcVo compareObj = (Icd10AmProcVo)obj;
		int retVal = 0;
		if (retVal == 0)
		{
			if(this.getAscii_short_desc() == null && compareObj.getAscii_short_desc() != null)
				return -1;
			if(this.getAscii_short_desc() != null && compareObj.getAscii_short_desc() == null)
				return 1;
			if(this.getAscii_short_desc() != null && compareObj.getAscii_short_desc() != null)
			{
				if(caseInsensitive)
					retVal = this.getAscii_short_desc().toLowerCase().compareTo(compareObj.getAscii_short_desc().toLowerCase());
				else
					retVal = this.getAscii_short_desc().compareTo(compareObj.getAscii_short_desc());
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
		if(this.code_id != null)
			count++;
		if(this.block != null)
			count++;
		if(this.ascii_desc != null)
			count++;
		if(this.ascii_short_desc != null)
			count++;
		if(this.effective_from != null)
			count++;
		if(this.inactive != null)
			count++;
		if(this.sex != null)
			count++;
		if(this.stype != null)
			count++;
		if(this.agel != null)
			count++;
		if(this.agelh != null)
			count++;
		if(this.atype != null)
			count++;
		return count;
	}
	public int countValueObjectFields()
	{
		return 11;
	}
	protected String code_id;
	protected ims.admin.vo.Icd10AmBlockVo block;
	protected String ascii_desc;
	protected String ascii_short_desc;
	protected ims.framework.utils.Date effective_from;
	protected ims.framework.utils.Date inactive;
	protected Integer sex;
	protected Integer stype;
	protected Integer agel;
	protected Integer agelh;
	protected Integer atype;
	private boolean isValidated = false;
	private boolean isBusy = false;
}