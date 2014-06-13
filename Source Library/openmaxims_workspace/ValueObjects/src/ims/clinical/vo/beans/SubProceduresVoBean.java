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

package ims.clinical.vo.beans;

public class SubProceduresVoBean extends ims.vo.ValueObjectBean
{
	public SubProceduresVoBean()
	{
	}
	public SubProceduresVoBean(ims.clinical.vo.SubProceduresVo vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.procedure = vo.getProcedure() == null ? null : (ims.core.vo.beans.ProcedureLiteVoBean)vo.getProcedure().getBean();
		this.subproceduredescription = vo.getSubProcedureDescription();
	}

	public void populate(ims.vo.ValueObjectBeanMap map, ims.clinical.vo.SubProceduresVo vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.procedure = vo.getProcedure() == null ? null : (ims.core.vo.beans.ProcedureLiteVoBean)vo.getProcedure().getBean(map);
		this.subproceduredescription = vo.getSubProcedureDescription();
	}

	public ims.clinical.vo.SubProceduresVo buildVo()
	{
		return this.buildVo(new ims.vo.ValueObjectBeanMap());
	}

	public ims.clinical.vo.SubProceduresVo buildVo(ims.vo.ValueObjectBeanMap map)
	{
		ims.clinical.vo.SubProceduresVo vo = null;
		if(map != null)
			vo = (ims.clinical.vo.SubProceduresVo)map.getValueObject(this);
		if(vo == null)
		{
			vo = new ims.clinical.vo.SubProceduresVo();
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
	public ims.core.vo.beans.ProcedureLiteVoBean getProcedure()
	{
		return this.procedure;
	}
	public void setProcedure(ims.core.vo.beans.ProcedureLiteVoBean value)
	{
		this.procedure = value;
	}
	public String getSubProcedureDescription()
	{
		return this.subproceduredescription;
	}
	public void setSubProcedureDescription(String value)
	{
		this.subproceduredescription = value;
	}

	private Integer id;
	private int version;
	private ims.core.vo.beans.ProcedureLiteVoBean procedure;
	private String subproceduredescription;
}