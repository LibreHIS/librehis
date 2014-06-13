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


public class ChartInvestigationCommentsVo extends ims.vo.ValueObject implements ims.vo.ImsCloneable, Comparable, ims.vo.interfaces.IChartValueInvestigation
{
	private static final long serialVersionUID = 1L;

	public ChartInvestigationCommentsVo()
	{
	}
	public ChartInvestigationCommentsVo(ims.core.vo.beans.ChartInvestigationCommentsVoBean bean)
	{
		this.investigationname = bean.getInvestigationName();
		this.laborderno = bean.getLabOrderNo();
		this.date = bean.getDate() == null ? null : bean.getDate().buildDateTime();
		this.investigationcomments = ims.core.vo.ChartValueCommentVoCollection.buildFromBeanCollection(bean.getInvestigationComments());
		this.displayflag = bean.getDisplayFlag();
		this.investigationid = bean.getInvestigationID();
		this.isdft = bean.getIsDFT();
		this.pathologyresultid = bean.getPathologyResultId();
	}
	public void populate(ims.vo.ValueObjectBeanMap map, ims.core.vo.beans.ChartInvestigationCommentsVoBean bean)
	{
		this.investigationname = bean.getInvestigationName();
		this.laborderno = bean.getLabOrderNo();
		this.date = bean.getDate() == null ? null : bean.getDate().buildDateTime();
		this.investigationcomments = ims.core.vo.ChartValueCommentVoCollection.buildFromBeanCollection(bean.getInvestigationComments());
		this.displayflag = bean.getDisplayFlag();
		this.investigationid = bean.getInvestigationID();
		this.isdft = bean.getIsDFT();
		this.pathologyresultid = bean.getPathologyResultId();
	}
	public ims.vo.ValueObjectBean getBean()
	{
		return this.getBean(new ims.vo.ValueObjectBeanMap());
	}
	public ims.vo.ValueObjectBean getBean(ims.vo.ValueObjectBeanMap map)
	{
		ims.core.vo.beans.ChartInvestigationCommentsVoBean bean = null;
		if(map != null)
			bean = (ims.core.vo.beans.ChartInvestigationCommentsVoBean)map.getValueObjectBean(this);
		if (bean == null)
		{
			bean = new ims.core.vo.beans.ChartInvestigationCommentsVoBean();
			map.addValueObjectBean(this, bean);
			bean.populate(map, this);
		}
		return bean;
	}
	public boolean getInvestigationNameIsNotNull()
	{
		return this.investigationname != null;
	}
	public String getInvestigationName()
	{
		return this.investigationname;
	}
	public static int getInvestigationNameMaxLength()
	{
		return 255;
	}
	public void setInvestigationName(String value)
	{
		this.isValidated = false;
		this.investigationname = value;
	}
	public boolean getLabOrderNoIsNotNull()
	{
		return this.laborderno != null;
	}
	public String getLabOrderNo()
	{
		return this.laborderno;
	}
	public static int getLabOrderNoMaxLength()
	{
		return 255;
	}
	public void setLabOrderNo(String value)
	{
		this.isValidated = false;
		this.laborderno = value;
	}
	public boolean getDateIsNotNull()
	{
		return this.date != null;
	}
	public ims.framework.utils.DateTime getDate()
	{
		return this.date;
	}
	public void setDate(ims.framework.utils.DateTime value)
	{
		this.isValidated = false;
		this.date = value;
	}
	public boolean getInvestigationCommentsIsNotNull()
	{
		return this.investigationcomments != null;
	}
	public ims.core.vo.ChartValueCommentVoCollection getInvestigationComments()
	{
		return this.investigationcomments;
	}
	public void setInvestigationComments(ims.core.vo.ChartValueCommentVoCollection value)
	{
		this.isValidated = false;
		this.investigationcomments = value;
	}
	public boolean getDisplayFlagIsNotNull()
	{
		return this.displayflag != null;
	}
	public String getDisplayFlag()
	{
		return this.displayflag;
	}
	public static int getDisplayFlagMaxLength()
	{
		return 255;
	}
	public void setDisplayFlag(String value)
	{
		this.isValidated = false;
		this.displayflag = value;
	}
	public boolean getInvestigationIDIsNotNull()
	{
		return this.investigationid != null;
	}
	public Integer getInvestigationID()
	{
		return this.investigationid;
	}
	public void setInvestigationID(Integer value)
	{
		this.isValidated = false;
		this.investigationid = value;
	}
	public boolean getIsDFTIsNotNull()
	{
		return this.isdft != null;
	}
	public Boolean getIsDFT()
	{
		return this.isdft;
	}
	public void setIsDFT(Boolean value)
	{
		this.isValidated = false;
		this.isdft = value;
	}
	public boolean getPathologyResultIdIsNotNull()
	{
		return this.pathologyresultid != null;
	}
	public Integer getPathologyResultId()
	{
		return this.pathologyresultid;
	}
	public void setPathologyResultId(Integer value)
	{
		this.isValidated = false;
		this.pathologyresultid = value;
	}
	public final String getIItemText()
	{
		return toString();
	}
	public final Integer getBoId() 
	{
		return null;
	}
	public final String getBoClassName()
	{
		return null;
	}
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		if(this.investigationname != null)
			sb.append(this.investigationname);
		return sb.toString();
	}
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(!(obj instanceof ChartInvestigationCommentsVo))
			return false;
		ChartInvestigationCommentsVo compareObj = (ChartInvestigationCommentsVo)obj;
		if(this.getInvestigationID() == null && compareObj.getInvestigationID() != null)
			return false;
		if(this.getInvestigationID() != null && compareObj.getInvestigationID() == null)
			return false;
		if(this.getInvestigationID() != null && compareObj.getInvestigationID() != null)
			return this.getInvestigationID().equals(compareObj.getInvestigationID());
		return super.equals(obj);
	}
	/**
	* IChartValueInvestigation
	*/
	public String getIChartValueInvestigationName()
	{
		return investigationname;
	}
	public String getIChartValueInvestigationLabOrderNo()
	{
		return laborderno;
	}
	public ims.framework.utils.DateTime getIChartValueDisplayDate()
	{
		return date;
	}
	public String getIChartValueDisplayFlag()
	{
		return displayflag;
	}
	public Integer getIChartValueInvestigationID()
	{
		return investigationid;
	}
	public ims.vo.interfaces.IChartValueComment[] getIChartValueComments()
	{
		if (investigationcomments == null)
			return new ims.vo.interfaces.IChartValueComment[0];
		
		ims.vo.interfaces.IChartValueComment[] result = new ims.vo.interfaces.IChartValueComment[investigationcomments.size()];
			
		for (int x = 0; x < investigationcomments.size(); x++)
		{
			result[x] = investigationcomments.get(x);
		}
	
		return result;
	}
	
	public Boolean getIChartValueInvestigationIsDFT() 
	{
		return isdft;
	}
	
	public Integer getIChartValuePathologyResultId()
	{
		return pathologyresultid;
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
	public Object clone()
	{
		if(this.isBusy)
			return this;
		this.isBusy = true;
	
		ChartInvestigationCommentsVo clone = new ChartInvestigationCommentsVo();
		
		clone.investigationname = this.investigationname;
		clone.laborderno = this.laborderno;
		if(this.date == null)
			clone.date = null;
		else
			clone.date = (ims.framework.utils.DateTime)this.date.clone();
		if(this.investigationcomments == null)
			clone.investigationcomments = null;
		else
			clone.investigationcomments = (ims.core.vo.ChartValueCommentVoCollection)this.investigationcomments.clone();
		clone.displayflag = this.displayflag;
		clone.investigationid = this.investigationid;
		clone.isdft = this.isdft;
		clone.pathologyresultid = this.pathologyresultid;
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
		if (!(ChartInvestigationCommentsVo.class.isAssignableFrom(obj.getClass())))
		{
			throw new ClassCastException("A ChartInvestigationCommentsVo object cannot be compared an Object of type " + obj.getClass().getName());
		}
		ChartInvestigationCommentsVo compareObj = (ChartInvestigationCommentsVo)obj;
		int retVal = 0;
		if (retVal == 0)
		{
			if(this.getDate() == null && compareObj.getDate() != null)
				return -1;
			if(this.getDate() != null && compareObj.getDate() == null)
				return 1;
			if(this.getDate() != null && compareObj.getDate() != null)
				retVal = this.getDate().compareTo(compareObj.getDate());
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
		if(this.investigationname != null)
			count++;
		if(this.laborderno != null)
			count++;
		if(this.date != null)
			count++;
		if(this.investigationcomments != null)
			count++;
		if(this.displayflag != null)
			count++;
		if(this.investigationid != null)
			count++;
		if(this.isdft != null)
			count++;
		if(this.pathologyresultid != null)
			count++;
		return count;
	}
	public int countValueObjectFields()
	{
		return 8;
	}
	protected String investigationname;
	protected String laborderno;
	protected ims.framework.utils.DateTime date;
	protected ims.core.vo.ChartValueCommentVoCollection investigationcomments;
	protected String displayflag;
	protected Integer investigationid;
	protected Boolean isdft;
	protected Integer pathologyresultid;
	private boolean isValidated = false;
	private boolean isBusy = false;
}