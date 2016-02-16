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

package ims.scheduling.vo.beans;

public class ProfileGenerationSettingsVoBean extends ims.vo.ValueObjectBean
{
	public ProfileGenerationSettingsVoBean()
	{
	}
	public ProfileGenerationSettingsVoBean(ims.scheduling.vo.ProfileGenerationSettingsVo vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.configuredjob = vo.getConfiguredJob() == null ? null : new ims.vo.RefVoBean(vo.getConfiguredJob().getBoId(), vo.getConfiguredJob().getBoVersion());
		this.sessionscount = vo.getSessionsCount() == null ? null : vo.getSessionsCount().getBeanCollection();
	}

	public void populate(ims.vo.ValueObjectBeanMap map, ims.scheduling.vo.ProfileGenerationSettingsVo vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.configuredjob = vo.getConfiguredJob() == null ? null : new ims.vo.RefVoBean(vo.getConfiguredJob().getBoId(), vo.getConfiguredJob().getBoVersion());
		this.sessionscount = vo.getSessionsCount() == null ? null : vo.getSessionsCount().getBeanCollection();
	}

	public ims.scheduling.vo.ProfileGenerationSettingsVo buildVo()
	{
		return this.buildVo(new ims.vo.ValueObjectBeanMap());
	}

	public ims.scheduling.vo.ProfileGenerationSettingsVo buildVo(ims.vo.ValueObjectBeanMap map)
	{
		ims.scheduling.vo.ProfileGenerationSettingsVo vo = null;
		if(map != null)
			vo = (ims.scheduling.vo.ProfileGenerationSettingsVo)map.getValueObject(this);
		if(vo == null)
		{
			vo = new ims.scheduling.vo.ProfileGenerationSettingsVo();
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
	public ims.vo.RefVoBean getConfiguredJob()
	{
		return this.configuredjob;
	}
	public void setConfiguredJob(ims.vo.RefVoBean value)
	{
		this.configuredjob = value;
	}
	public ims.scheduling.vo.beans.ProfileSessionsGeneratedCountVoBean[] getSessionsCount()
	{
		return this.sessionscount;
	}
	public void setSessionsCount(ims.scheduling.vo.beans.ProfileSessionsGeneratedCountVoBean[] value)
	{
		this.sessionscount = value;
	}

	private Integer id;
	private int version;
	private ims.vo.RefVoBean configuredjob;
	private ims.scheduling.vo.beans.ProfileSessionsGeneratedCountVoBean[] sessionscount;
}