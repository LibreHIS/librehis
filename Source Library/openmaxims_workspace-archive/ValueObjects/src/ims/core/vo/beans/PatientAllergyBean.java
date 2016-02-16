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

public class PatientAllergyBean extends ims.vo.ValueObjectBean
{
	public PatientAllergyBean()
	{
	}
	public PatientAllergyBean(ims.core.vo.PatientAllergy vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.allergen = vo.getAllergen() == null ? null : (ims.core.vo.beans.AllergenVoBean)vo.getAllergen().getBean();
		this.sourceofinformation = vo.getSourceofInformation() == null ? null : (ims.vo.LookupInstanceBean)vo.getSourceofInformation().getBean();
		this.whenidentified = vo.getWhenIdentified() == null ? null : (ims.framework.utils.beans.PartialDateBean)vo.getWhenIdentified().getBean();
		this.comments = vo.getComments();
		this.iscurrentlyactiveallergy = vo.getIsCurrentlyActiveAllergy();
		this.reactions = vo.getReactions() == null ? null : vo.getReactions().getBeanCollection();
		this.sysinfo = vo.getSysInfo() == null ? null : (ims.vo.SysInfoBean)vo.getSysInfo().getBean();
		this.allergendescription = vo.getAllergenDescription();
		this.allergentype = vo.getAllergenType() == null ? null : (ims.vo.LookupInstanceBean)vo.getAllergenType().getBean();
		this.authoringinformation = vo.getAuthoringInformation() == null ? null : (ims.core.vo.beans.AuthoringInformationVoBean)vo.getAuthoringInformation().getBean();
		this.patient = vo.getPatient() == null ? null : new ims.vo.RefVoBean(vo.getPatient().getBoId(), vo.getPatient().getBoVersion());
		this.inactivationauthoringinfo = vo.getInactivationAuthoringInfo() == null ? null : (ims.core.vo.beans.AuthoringInformationVoBean)vo.getInactivationAuthoringInfo().getBean();
		this.recordinginformation = vo.getRecordingInformation() == null ? null : (ims.core.vo.beans.RecordingUserInformationVoBean)vo.getRecordingInformation().getBean();
	}

	public void populate(ims.vo.ValueObjectBeanMap map, ims.core.vo.PatientAllergy vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.allergen = vo.getAllergen() == null ? null : (ims.core.vo.beans.AllergenVoBean)vo.getAllergen().getBean(map);
		this.sourceofinformation = vo.getSourceofInformation() == null ? null : (ims.vo.LookupInstanceBean)vo.getSourceofInformation().getBean();
		this.whenidentified = vo.getWhenIdentified() == null ? null : (ims.framework.utils.beans.PartialDateBean)vo.getWhenIdentified().getBean();
		this.comments = vo.getComments();
		this.iscurrentlyactiveallergy = vo.getIsCurrentlyActiveAllergy();
		this.reactions = vo.getReactions() == null ? null : vo.getReactions().getBeanCollection();
		this.sysinfo = vo.getSysInfo() == null ? null : (ims.vo.SysInfoBean)vo.getSysInfo().getBean();
		this.allergendescription = vo.getAllergenDescription();
		this.allergentype = vo.getAllergenType() == null ? null : (ims.vo.LookupInstanceBean)vo.getAllergenType().getBean();
		this.authoringinformation = vo.getAuthoringInformation() == null ? null : (ims.core.vo.beans.AuthoringInformationVoBean)vo.getAuthoringInformation().getBean(map);
		this.patient = vo.getPatient() == null ? null : new ims.vo.RefVoBean(vo.getPatient().getBoId(), vo.getPatient().getBoVersion());
		this.inactivationauthoringinfo = vo.getInactivationAuthoringInfo() == null ? null : (ims.core.vo.beans.AuthoringInformationVoBean)vo.getInactivationAuthoringInfo().getBean(map);
		this.recordinginformation = vo.getRecordingInformation() == null ? null : (ims.core.vo.beans.RecordingUserInformationVoBean)vo.getRecordingInformation().getBean(map);
	}

	public ims.core.vo.PatientAllergy buildVo()
	{
		return this.buildVo(new ims.vo.ValueObjectBeanMap());
	}

	public ims.core.vo.PatientAllergy buildVo(ims.vo.ValueObjectBeanMap map)
	{
		ims.core.vo.PatientAllergy vo = null;
		if(map != null)
			vo = (ims.core.vo.PatientAllergy)map.getValueObject(this);
		if(vo == null)
		{
			vo = new ims.core.vo.PatientAllergy();
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
	public ims.core.vo.beans.AllergenVoBean getAllergen()
	{
		return this.allergen;
	}
	public void setAllergen(ims.core.vo.beans.AllergenVoBean value)
	{
		this.allergen = value;
	}
	public ims.vo.LookupInstanceBean getSourceofInformation()
	{
		return this.sourceofinformation;
	}
	public void setSourceofInformation(ims.vo.LookupInstanceBean value)
	{
		this.sourceofinformation = value;
	}
	public ims.framework.utils.beans.PartialDateBean getWhenIdentified()
	{
		return this.whenidentified;
	}
	public void setWhenIdentified(ims.framework.utils.beans.PartialDateBean value)
	{
		this.whenidentified = value;
	}
	public String getComments()
	{
		return this.comments;
	}
	public void setComments(String value)
	{
		this.comments = value;
	}
	public Boolean getIsCurrentlyActiveAllergy()
	{
		return this.iscurrentlyactiveallergy;
	}
	public void setIsCurrentlyActiveAllergy(Boolean value)
	{
		this.iscurrentlyactiveallergy = value;
	}
	public ims.core.vo.beans.PatientAllergyReactionVoBean[] getReactions()
	{
		return this.reactions;
	}
	public void setReactions(ims.core.vo.beans.PatientAllergyReactionVoBean[] value)
	{
		this.reactions = value;
	}
	public ims.vo.SysInfoBean getSysInfo()
	{
		return this.sysinfo;
	}
	public void setSysInfo(ims.vo.SysInfoBean value)
	{
		this.sysinfo = value;
	}
	public String getAllergenDescription()
	{
		return this.allergendescription;
	}
	public void setAllergenDescription(String value)
	{
		this.allergendescription = value;
	}
	public ims.vo.LookupInstanceBean getAllergenType()
	{
		return this.allergentype;
	}
	public void setAllergenType(ims.vo.LookupInstanceBean value)
	{
		this.allergentype = value;
	}
	public ims.core.vo.beans.AuthoringInformationVoBean getAuthoringInformation()
	{
		return this.authoringinformation;
	}
	public void setAuthoringInformation(ims.core.vo.beans.AuthoringInformationVoBean value)
	{
		this.authoringinformation = value;
	}
	public ims.vo.RefVoBean getPatient()
	{
		return this.patient;
	}
	public void setPatient(ims.vo.RefVoBean value)
	{
		this.patient = value;
	}
	public ims.core.vo.beans.AuthoringInformationVoBean getInactivationAuthoringInfo()
	{
		return this.inactivationauthoringinfo;
	}
	public void setInactivationAuthoringInfo(ims.core.vo.beans.AuthoringInformationVoBean value)
	{
		this.inactivationauthoringinfo = value;
	}
	public ims.core.vo.beans.RecordingUserInformationVoBean getRecordingInformation()
	{
		return this.recordinginformation;
	}
	public void setRecordingInformation(ims.core.vo.beans.RecordingUserInformationVoBean value)
	{
		this.recordinginformation = value;
	}

	private Integer id;
	private int version;
	private ims.core.vo.beans.AllergenVoBean allergen;
	private ims.vo.LookupInstanceBean sourceofinformation;
	private ims.framework.utils.beans.PartialDateBean whenidentified;
	private String comments;
	private Boolean iscurrentlyactiveallergy;
	private ims.core.vo.beans.PatientAllergyReactionVoBean[] reactions;
	private ims.vo.SysInfoBean sysinfo;
	private String allergendescription;
	private ims.vo.LookupInstanceBean allergentype;
	private ims.core.vo.beans.AuthoringInformationVoBean authoringinformation;
	private ims.vo.RefVoBean patient;
	private ims.core.vo.beans.AuthoringInformationVoBean inactivationauthoringinfo;
	private ims.core.vo.beans.RecordingUserInformationVoBean recordinginformation;
}