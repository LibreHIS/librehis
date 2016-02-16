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

package ims.core.vo.beans;

public class DataSetTypeVoBean extends ims.vo.ValueObjectBean
{
	public DataSetTypeVoBean()
	{
	}
	public DataSetTypeVoBean(ims.core.vo.DataSetTypeVo vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.name = vo.getName();
		this.category = vo.getCategory() == null ? null : (ims.vo.LookupInstanceBean)vo.getCategory().getBean();
		this.maxfactor = vo.getMaxFactor();
		this.minfactor = vo.getMinFactor();
		this.defaultlinetype = vo.getDefaultLineType() == null ? null : (ims.vo.LookupInstanceBean)vo.getDefaultLineType().getBean();
		this.deflinecolpticon = vo.getDefLineColPtIcon() == null ? null : (ims.vo.LookupInstanceBean)vo.getDefLineColPtIcon().getBean();
		this.normalbandmaxvaluemale = vo.getNormalBandMaxValueMale();
		this.normalbandminvaluemale = vo.getNormalBandMinValueMale();
		this.normalbandmaxvaluefemale = vo.getNormalBandMaxValueFemale();
		this.normalbandminvaluefemale = vo.getNormalBandMinValueFemale();
		this.isactive = vo.getIsActive();
		this.unitofmeasure = vo.getUnitOfMeasure() == null ? null : (ims.vo.LookupInstanceBean)vo.getUnitOfMeasure().getBean();
	}

	public void populate(ims.vo.ValueObjectBeanMap map, ims.core.vo.DataSetTypeVo vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.name = vo.getName();
		this.category = vo.getCategory() == null ? null : (ims.vo.LookupInstanceBean)vo.getCategory().getBean();
		this.maxfactor = vo.getMaxFactor();
		this.minfactor = vo.getMinFactor();
		this.defaultlinetype = vo.getDefaultLineType() == null ? null : (ims.vo.LookupInstanceBean)vo.getDefaultLineType().getBean();
		this.deflinecolpticon = vo.getDefLineColPtIcon() == null ? null : (ims.vo.LookupInstanceBean)vo.getDefLineColPtIcon().getBean();
		this.normalbandmaxvaluemale = vo.getNormalBandMaxValueMale();
		this.normalbandminvaluemale = vo.getNormalBandMinValueMale();
		this.normalbandmaxvaluefemale = vo.getNormalBandMaxValueFemale();
		this.normalbandminvaluefemale = vo.getNormalBandMinValueFemale();
		this.isactive = vo.getIsActive();
		this.unitofmeasure = vo.getUnitOfMeasure() == null ? null : (ims.vo.LookupInstanceBean)vo.getUnitOfMeasure().getBean();
	}

	public ims.core.vo.DataSetTypeVo buildVo()
	{
		return this.buildVo(new ims.vo.ValueObjectBeanMap());
	}

	public ims.core.vo.DataSetTypeVo buildVo(ims.vo.ValueObjectBeanMap map)
	{
		ims.core.vo.DataSetTypeVo vo = null;
		if(map != null)
			vo = (ims.core.vo.DataSetTypeVo)map.getValueObject(this);
		if(vo == null)
		{
			vo = new ims.core.vo.DataSetTypeVo();
			map.addValueObject(this, vo);
			vo.populate(map, this);
		}
		return vo;
	}

	public Integer getId()
	{
		return this.id;
	}
	public void setId(Integer value)
	{
		this.id = value;
	}
	public int getVersion()
	{
		return this.version;
	}
	public void setVersion(int value)
	{
		this.version = value;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String value)
	{
		this.name = value;
	}
	public ims.vo.LookupInstanceBean getCategory()
	{
		return this.category;
	}
	public void setCategory(ims.vo.LookupInstanceBean value)
	{
		this.category = value;
	}
	public Integer getMaxFactor()
	{
		return this.maxfactor;
	}
	public void setMaxFactor(Integer value)
	{
		this.maxfactor = value;
	}
	public Integer getMinFactor()
	{
		return this.minfactor;
	}
	public void setMinFactor(Integer value)
	{
		this.minfactor = value;
	}
	public ims.vo.LookupInstanceBean getDefaultLineType()
	{
		return this.defaultlinetype;
	}
	public void setDefaultLineType(ims.vo.LookupInstanceBean value)
	{
		this.defaultlinetype = value;
	}
	public ims.vo.LookupInstanceBean getDefLineColPtIcon()
	{
		return this.deflinecolpticon;
	}
	public void setDefLineColPtIcon(ims.vo.LookupInstanceBean value)
	{
		this.deflinecolpticon = value;
	}
	public Float getNormalBandMaxValueMale()
	{
		return this.normalbandmaxvaluemale;
	}
	public void setNormalBandMaxValueMale(Float value)
	{
		this.normalbandmaxvaluemale = value;
	}
	public Float getNormalBandMinValueMale()
	{
		return this.normalbandminvaluemale;
	}
	public void setNormalBandMinValueMale(Float value)
	{
		this.normalbandminvaluemale = value;
	}
	public Float getNormalBandMaxValueFemale()
	{
		return this.normalbandmaxvaluefemale;
	}
	public void setNormalBandMaxValueFemale(Float value)
	{
		this.normalbandmaxvaluefemale = value;
	}
	public Float getNormalBandMinValueFemale()
	{
		return this.normalbandminvaluefemale;
	}
	public void setNormalBandMinValueFemale(Float value)
	{
		this.normalbandminvaluefemale = value;
	}
	public Boolean getIsActive()
	{
		return this.isactive;
	}
	public void setIsActive(Boolean value)
	{
		this.isactive = value;
	}
	public ims.vo.LookupInstanceBean getUnitOfMeasure()
	{
		return this.unitofmeasure;
	}
	public void setUnitOfMeasure(ims.vo.LookupInstanceBean value)
	{
		this.unitofmeasure = value;
	}

	private Integer id;
	private int version;
	private String name;
	private ims.vo.LookupInstanceBean category;
	private Integer maxfactor;
	private Integer minfactor;
	private ims.vo.LookupInstanceBean defaultlinetype;
	private ims.vo.LookupInstanceBean deflinecolpticon;
	private Float normalbandmaxvaluemale;
	private Float normalbandminvaluemale;
	private Float normalbandmaxvaluefemale;
	private Float normalbandminvaluefemale;
	private Boolean isactive;
	private ims.vo.LookupInstanceBean unitofmeasure;
}