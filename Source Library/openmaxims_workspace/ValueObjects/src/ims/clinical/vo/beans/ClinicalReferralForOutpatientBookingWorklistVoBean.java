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

package ims.clinical.vo.beans;

public class ClinicalReferralForOutpatientBookingWorklistVoBean extends ims.vo.ValueObjectBean
{
	public ClinicalReferralForOutpatientBookingWorklistVoBean()
	{
	}
	public ClinicalReferralForOutpatientBookingWorklistVoBean(ims.clinical.vo.ClinicalReferralForOutpatientBookingWorklistVo vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.patient = vo.getPatient() == null ? null : (ims.core.vo.beans.PatientForOutpatientBookingWorklistVoBean)vo.getPatient().getBean();
		this.currentreferralstatus = vo.getCurrentReferralStatus() == null ? null : (ims.clinical.vo.beans.InternalReferralStatusForBookingWorklistVoBean)vo.getCurrentReferralStatus().getBean();
		this.refertohcp = vo.getReferToHCP() == null ? null : (ims.core.vo.beans.HcpLiteVoBean)vo.getReferToHCP().getBean();
		this.urgentreferral = vo.getUrgentReferral();
		this.datedecisiontorefer = vo.getDateDecisionToRefer() == null ? null : (ims.framework.utils.beans.DateBean)vo.getDateDecisionToRefer().getBean();
		this.referringhcp = vo.getReferringHCP() == null ? null : (ims.core.vo.beans.HcpLiteVoBean)vo.getReferringHCP().getBean();
		this.refertoservice = vo.getReferToService() == null ? null : (ims.clinical.vo.beans.ServiceConfigIntReferralBookingListVoBean)vo.getReferToService().getBean();
		this.referraltype = vo.getReferralType() == null ? null : (ims.vo.LookupInstanceBean)vo.getReferralType().getBean();
	}

	public void populate(ims.vo.ValueObjectBeanMap map, ims.clinical.vo.ClinicalReferralForOutpatientBookingWorklistVo vo)
	{
		this.id = vo.getBoId();
		this.version = vo.getBoVersion();
		this.patient = vo.getPatient() == null ? null : (ims.core.vo.beans.PatientForOutpatientBookingWorklistVoBean)vo.getPatient().getBean(map);
		this.currentreferralstatus = vo.getCurrentReferralStatus() == null ? null : (ims.clinical.vo.beans.InternalReferralStatusForBookingWorklistVoBean)vo.getCurrentReferralStatus().getBean(map);
		this.refertohcp = vo.getReferToHCP() == null ? null : (ims.core.vo.beans.HcpLiteVoBean)vo.getReferToHCP().getBean(map);
		this.urgentreferral = vo.getUrgentReferral();
		this.datedecisiontorefer = vo.getDateDecisionToRefer() == null ? null : (ims.framework.utils.beans.DateBean)vo.getDateDecisionToRefer().getBean();
		this.referringhcp = vo.getReferringHCP() == null ? null : (ims.core.vo.beans.HcpLiteVoBean)vo.getReferringHCP().getBean(map);
		this.refertoservice = vo.getReferToService() == null ? null : (ims.clinical.vo.beans.ServiceConfigIntReferralBookingListVoBean)vo.getReferToService().getBean(map);
		this.referraltype = vo.getReferralType() == null ? null : (ims.vo.LookupInstanceBean)vo.getReferralType().getBean();
	}

	public ims.clinical.vo.ClinicalReferralForOutpatientBookingWorklistVo buildVo()
	{
		return this.buildVo(new ims.vo.ValueObjectBeanMap());
	}

	public ims.clinical.vo.ClinicalReferralForOutpatientBookingWorklistVo buildVo(ims.vo.ValueObjectBeanMap map)
	{
		ims.clinical.vo.ClinicalReferralForOutpatientBookingWorklistVo vo = null;
		if(map != null)
			vo = (ims.clinical.vo.ClinicalReferralForOutpatientBookingWorklistVo)map.getValueObject(this);
		if(vo == null)
		{
			vo = new ims.clinical.vo.ClinicalReferralForOutpatientBookingWorklistVo();
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
	public ims.core.vo.beans.PatientForOutpatientBookingWorklistVoBean getPatient()
	{
		return this.patient;
	}
	public void setPatient(ims.core.vo.beans.PatientForOutpatientBookingWorklistVoBean value)
	{
		this.patient = value;
	}
	public ims.clinical.vo.beans.InternalReferralStatusForBookingWorklistVoBean getCurrentReferralStatus()
	{
		return this.currentreferralstatus;
	}
	public void setCurrentReferralStatus(ims.clinical.vo.beans.InternalReferralStatusForBookingWorklistVoBean value)
	{
		this.currentreferralstatus = value;
	}
	public ims.core.vo.beans.HcpLiteVoBean getReferToHCP()
	{
		return this.refertohcp;
	}
	public void setReferToHCP(ims.core.vo.beans.HcpLiteVoBean value)
	{
		this.refertohcp = value;
	}
	public Boolean getUrgentReferral()
	{
		return this.urgentreferral;
	}
	public void setUrgentReferral(Boolean value)
	{
		this.urgentreferral = value;
	}
	public ims.framework.utils.beans.DateBean getDateDecisionToRefer()
	{
		return this.datedecisiontorefer;
	}
	public void setDateDecisionToRefer(ims.framework.utils.beans.DateBean value)
	{
		this.datedecisiontorefer = value;
	}
	public ims.core.vo.beans.HcpLiteVoBean getReferringHCP()
	{
		return this.referringhcp;
	}
	public void setReferringHCP(ims.core.vo.beans.HcpLiteVoBean value)
	{
		this.referringhcp = value;
	}
	public ims.clinical.vo.beans.ServiceConfigIntReferralBookingListVoBean getReferToService()
	{
		return this.refertoservice;
	}
	public void setReferToService(ims.clinical.vo.beans.ServiceConfigIntReferralBookingListVoBean value)
	{
		this.refertoservice = value;
	}
	public ims.vo.LookupInstanceBean getReferralType()
	{
		return this.referraltype;
	}
	public void setReferralType(ims.vo.LookupInstanceBean value)
	{
		this.referraltype = value;
	}

	private Integer id;
	private int version;
	private ims.core.vo.beans.PatientForOutpatientBookingWorklistVoBean patient;
	private ims.clinical.vo.beans.InternalReferralStatusForBookingWorklistVoBean currentreferralstatus;
	private ims.core.vo.beans.HcpLiteVoBean refertohcp;
	private Boolean urgentreferral;
	private ims.framework.utils.beans.DateBean datedecisiontorefer;
	private ims.core.vo.beans.HcpLiteVoBean referringhcp;
	private ims.clinical.vo.beans.ServiceConfigIntReferralBookingListVoBean refertoservice;
	private ims.vo.LookupInstanceBean referraltype;
}