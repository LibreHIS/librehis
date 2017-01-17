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
// This code was generated by John MacEnri using IMS Development Environment (version 1.20 build 40727.1400)
// Copyright (C) 1995-2004 IMS MAXIMS plc. All rights reserved.

package ims.admin.domain.impl;

import ims.admin.vo.AppRoleShortVoCollection;
import ims.admin.vo.AppUserShortVo;
import ims.admin.vo.AppUserShortVoCollection;
import ims.admin.vo.AppUserVo;
import ims.admin.vo.UserDTOVo;
import ims.admin.vo.UserEmailAccountVo;
import ims.admin.vo.domain.AppRoleShortVoAssembler;
import ims.admin.vo.domain.AppUserShortVoAssembler;
import ims.admin.vo.domain.AppUserVoAssembler;
import ims.admin.vo.domain.UserEmailAccountVoAssembler;
import ims.configuration.gen.ConfigFlag;
import ims.configuration.Configuration;
import ims.core.configuration.domain.objects.AppUser;
import ims.core.configuration.vo.AppUserRefVo;
import ims.core.vo.MemberOfStaffShortVoCollection;
import ims.core.vo.domain.MemberOfStaffShortVoAssembler;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DTODomainInterfaceException;
import ims.domain.exceptions.DomainInterfaceException;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.exceptions.UniqueKeyViolationException;
import ims.domain.exceptions.UnqViolationUncheckedException;
import ims.domain.hibernate3.IMSCriteria;
import ims.dto.DTODomainImplementation;
import ims.dto.DtoErrorCode;
import ims.dto.Result;
import ims.dto.ResultException;
import ims.dto.client.App_users;
import ims.dto.client.User;
import ims.dto.client.App_users.App_usersRecord;
import ims.dto.client.User.UserRecord;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.interfaces.IAppRoleLight;
import ims.framework.utils.Date;
import ims.framework.utils.DateFormat;
import ims.framework.utils.DateTime;
import ims.framework.utils.TimeFormat;
import ims.core.resource.people.domain.objects.MemberOfStaff;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ApplicationUsersImpl extends DTODomainImplementation implements ims.admin.domain.ApplicationUsers, ims.domain.impl.Transactional
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG	= Logger.getLogger(ApplicationUsersImpl.class);
	
	
	/**
	 * WDEV-12720
	 * Function used to search for Members of Staff not linked to Application Users
	 * matching 'mosSurname' and 'mosForename' strings passed
	 * Strings passed will be split into words by " " (space) separator and then each
	 * of these words is required to be CONTAINED in surname or fore name 
	 */
	public MemberOfStaffShortVoCollection listMos(String mosSurname, String mosForename)
	{
		DomainFactory factory = getDomainFactory();
		
		StringBuilder query = new StringBuilder();

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		query.append("select m from  MemberOfStaff m where m.id not in ( select au.mos.id from AppUser au where au.mos is not null) ");

		/* TODO MSSQL case - query.append(" and m.isActive = 1"); */
		query.append(" and m.isActive = true");

		if (mosSurname != null && mosSurname.length() > 0)
		{
			String[] surnames = mosSurname.split("\\s+");
			
			for (int i = 0; i < surnames.length; i++)
			{
				surnames[i] = surnames[i].replaceAll("[^a-zA-Z]", "");
				
				if (surnames[i].length() > 0)
				{
					query.append(" and (m.name.upperSurname like :SURNAME").append(i).append(")");
					
					markers.add("SURNAME" + i);
					values.add("%" + surnames[i].toUpperCase() + "%");
				}
			}
		}
		
		if (mosForename != null && mosForename.length() > 0)
		{
			String[] forenames = mosForename.split("\\s+");
			
			for (int i = 0; i < forenames.length; i++)
			{
				forenames[i] = forenames[i].replaceAll("[^a-zA-Z]", "");
				
				if (forenames[i].length() > 0)
				{
					query.append(" and (m.name.upperForename like :FORENAME").append(i).append(")");
					
					markers.add("FORENAME" + i);
					values.add("%" + forenames[i].toUpperCase() + "%");
				}
			}
		}

		return MemberOfStaffShortVoAssembler.createMemberOfStaffShortVoCollectionFromMemberOfStaff(factory.find(query.toString(), markers, values)).sort();
	}

	public AppUserVo updateAppUser(AppUserVo appUser) throws StaleObjectException
	{
		DomainFactory factory = getDomainFactory();
		AppUser domUser = AppUserVoAssembler.extractAppUser(factory, appUser);
		factory.save(domUser);
		return AppUserVoAssembler.create(domUser);
	}
	
	public AppUserVo saveAppUser(AppUserVo appUserItem, Boolean replicateToDTO, ims.core.vo.MemberOfStaffShortVo memberOfStaffOld) throws StaleObjectException , UniqueKeyViolationException, DTODomainInterfaceException
	{
		if (!appUserItem.isValidated())
		{
			throw new DomainRuntimeException("Application User VO has not been validated.");
		}
		
		DomainFactory factory = getDomainFactory();		
		AppUser domUser = AppUserVoAssembler.extractAppUser(factory, appUserItem);
		//wdev-13963
		if(memberOfStaffOld != null)
		{
			MemberOfStaff domMemberOfStaff = (MemberOfStaff)factory.getDomainObject(MemberOfStaff.class,memberOfStaffOld.getID_MemberOfStaff());
			if(domMemberOfStaff != null)
			{
				domMemberOfStaff.setAppUser(null);
				factory.save(domMemberOfStaff);
			}
		}
		//------------
		if (domUser.getMos() != null)
			domUser.getMos().setAppUser(domUser);
		
		try
		{
			factory.save(domUser);
		}
		catch(UnqViolationUncheckedException e)
		{
			throw new UniqueKeyViolationException("A User with this name already exists within the system. Duplicate Usernames not allowed.", e);
		}
		
		// wdev-6512 - Replication to Hearts uses the User Service
		if (ConfigFlag.DOM.DTO_REPLICATE_APPUSERS.getValue() && replicateToDTO)
		{
			if (ConfigFlag.DTO.USER_REPLICATION_SERVICE.getValue().equals("USER"))
			{
				IAppRoleLight role = appUserItem.getAppRole(1);
				replicateUserToHeartsDtoServer(domUser, role);
			}
			// wdev-10731 - For CCO, we need to replicate to both HEARTS and JavaNas
			// so both AppUsers and User services need to be executed.
			// Had attempted to make both calls in one, but imx's differ so much this was
			// the easiest approach
			else if (ConfigFlag.DTO.USER_REPLICATION_SERVICE.getValue().equals("BOTH"))
			{
				IAppRoleLight role = appUserItem.getAppRole(1);
				replicateUserToHeartsDtoServer(domUser, role);
				replicateUserToDtoServer(domUser);
			}
			else
				replicateUserToDtoServer(domUser);
		} 
		return AppUserVoAssembler.create(domUser);
	}

	private void replicateUserToDtoServer(AppUser user) throws DTODomainInterfaceException
	{
		if (user.getUsername() == null || user.getUsername().length() == 0)
			throw new DomainRuntimeException("Username cannot be empty");
		
		App_users userRec = (App_users)getDTOInstance(App_users.class);
		userRec.Filter.clear();
		userRec.Filter.Uname = user.getUsername();
		userRec.Filter.User_id = user.getId() + "";
		
		Result res = userRec.get();
		if (res != null && res.getId() != DtoErrorCode.NO_DATA_FOUND)
		{
			throw new DTODomainInterfaceException(res.getId(), "Error occurred replicating user to DTO server." + res.getMessage());
		}

		if (res != null && res.getId() == DtoErrorCode.NO_DATA_FOUND)
		{
			userRec.DataCollection.add();
			copyUserToDto(userRec, user);
			res = userRec.insert();
			if (res != null && res.getId() < 0 )
			{
				throw new DTODomainInterfaceException(res.getId(), "Error occurred replicating user to DTO server." + res.getMessage());
			}
		}
		else
		{
			copyUserToDto(userRec, user);
			res = userRec.update();
			if (res != null && res.getId() != DtoErrorCode.NO_DATA_FOUND)
			{
				throw new DTODomainInterfaceException(res.getId(), "Error occurred replicating user to DTO server." + res.getMessage());
			}
		}
	}
	
	private void copyUserToDto(App_users userRec, AppUser user)
	{
		App_usersRecord rec = userRec.DataCollection.get(0);
		
		if (user.getId() != null)
			rec.User_id = user.getId() + "";
		rec.Uname = user.getUsername();		
		rec.Upass = user.getPassword();
		rec.Dbname = "imsuser";
		rec.Dbpass = "welcome";
		if (user.getSystemInformation().getCreationDateTime() != null)			
		{
			rec.Rdat = new Date(user.getSystemInformation().getCreationDateTime()).toString(DateFormat.ISO);
			rec.Rtim = new DateTime(user.getSystemInformation().getCreationDateTime()).getTime().toString(TimeFormat.FLAT6);
		}
		rec.Rusr = getUserId(user.getSystemInformation().getCreationUser());

		if (user.getSystemInformation().getLastUpdateDateTime() != null)
		{
			rec.Udat = new Date(user.getSystemInformation().getLastUpdateDateTime()).toString(DateFormat.ISO);
			rec.Utim = new DateTime(user.getSystemInformation().getLastUpdateDateTime()).getTime().toString(TimeFormat.FLAT6);
		}
		rec.Uusr = getUserId(user.getSystemInformation().getLastUpdateUser());
		rec.Comment = "Replicated from Maxims Web Application";
		
		if (user.getEffectiveFrom() != null)
			rec.Effr = new Date(user.getEffectiveFrom()).toString(DateFormat.ISO);	
		else
			rec.Effr = new Date().toString(DateFormat.ISO);
		if (user.getEffectiveTo() != null)
			rec.Efft = new Date(user.getEffectiveTo()).toString(DateFormat.ISO);			
		if (user.getPwdExpDate() != null)
			rec.Passwd_exp = new Date(user.getPwdExpDate()).toString(DateFormat.ISO);			
		if (user.isIsActive() != null && user.isIsActive().booleanValue() == false)
			rec.Active = "I";
		else
			rec.Active = "A";		
	}
	
	public void replicateUserToHeartsDtoServer(AppUser user, IAppRoleLight role) throws DTODomainInterfaceException
	{
		if (user.getUsername() == null || user.getUsername().length() == 0)
			throw new DomainRuntimeException("Username cannot be empty");
		
		// wdev-8744 - We only want to replicate the user if the password is not DUMMY
		// this is set in Logic as the database insists on a password even though we
		// don't want one for existing Hearts Users
		if (user.getPassword().equals("DUMMY"))
			return;
		
		User userRec=null;
		try
		{
			userRec = (User)getDTOInstance(User.class);
		}
		catch (ResultException e)
		{
			throw new DTODomainInterfaceException(-1, "Error occurred getting DTOInstance. " + e.getMessage());
		}

		// Hearts User Service does not handle GET call, so we need to do a Insert, if duplicate error, try update
		userRec.DataCollection.add();
		copyUserToHeartsDto(userRec, user, role);
		Result res = userRec.insert();
		if (res != null && res.getId() < 0 && res.getId() != DtoErrorCode.DUP_KEY_ERROR) // Insert Failed, try update
		{
				throw new DTODomainInterfaceException(res.getId(), "Error occurred replicating (insert) user to DTO server. (" + res.getId() + ")" + res.getMessage());
		}
		else if (res != null && res.getId() == DtoErrorCode.DUP_KEY_ERROR)  // Update as already there
		{
			res = userRec.update();
			if (res != null && res.getId() != DtoErrorCode.NO_DATA_FOUND)
			{
				throw new DTODomainInterfaceException(res.getId(), "Error occurred replicating (update) user to DTO server. (" + res.getId() + ")" + res.getMessage());
			}
		}
	}
	
	private void copyUserToHeartsDto(User userRec, AppUser user, IAppRoleLight role)
	{
		UserRecord rec = userRec.DataCollection.get(0);

		rec.User = user.getUsername();	
		
		if(user.getEncodedPassword() != null)
			rec.Pass = Configuration.decryptPasswordWithCloak(user.getEncodedPassword());
		
		rec.Hpcd = "NA";
		rec.Maxims="Y";
		// If this user has been assigned the administrator role, set opt = SUPER - admin in Hearts
		if (role != null)
			rec.Opt="SUPER";
	}


	private String getUserId(String userName)
	{
		if (userName == null)
			return "";
		
		AppUser createUser = AppUser.getAppUserFromUsername(this.getDomainFactory(), userName);
		if (createUser == null)
			return "";
			
		return createUser.getId().toString();
	}

	public AppUserShortVoCollection listAppUsers(AppUserShortVo appUserFilter) 
	{
		DomainFactory factory = getDomainFactory();
		String andStr = " ";
		StringBuffer clause = new StringBuffer();
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		String hql = "  from AppUser u ";
		
		boolean mosRequired = !ConfigFlag.GEN.ALLOW_NON_MOS_USERS.getValue();

		if (appUserFilter.getUsername() != null && appUserFilter.getUsername().length() > 0)
		{
			if (mosRequired)
			{
				hql = " select u from MemberOfStaff mos inner join mos.appUser u ";
				clause.append(" upper(mos.appUser.username) like :username");//name.upperSurname like :username");
				names.add("username");
				values.add(appUserFilter.getUsername().trim().toUpperCase() + "%");		
				andStr = " and ";				
			}
			else
			{
				hql = " from AppUser u ";
				clause.append(" upper(u.username) like :username");
				names.add("username");
				values.add("%" + appUserFilter.getUsername().trim().toUpperCase() + "%");
				andStr = " and ";
			}
		}		
		else if (appUserFilter.getUsername() == null)
		{
			if (mosRequired)
				hql = " select u from MemberOfStaff mos inner join mos.appUser u ";
		}
			
		if (appUserFilter.getIsActive() != null)
		{
			clause.append(andStr + " u.isActive = :isActive" );
			names.add("isActive");
			values.add(appUserFilter.getIsActive());		
			andStr = " and ";
		}
		
		if (appUserFilter.getLockedIsNotNull())
		{
			clause.append(andStr + " u.locked = :isLocked" );
			names.add("isLocked");
			values.add(appUserFilter.getLocked());		
			andStr = " and ";
		}
				
		clause.append(andStr).append(" u.username != 'xxxxx'");  // wdev-7907
		andStr=" and ";

		if (andStr.equals(" and "))
		{
			hql += " where ";
		}
		hql += clause.toString();

		AppUserShortVoCollection ret = new AppUserShortVoCollection();
		List l = factory.find(hql, names, values);
		for (int i = 0; i < l.size(); i++)
		{
			AppUser domUser = (AppUser)l.get(i);
			AppUserShortVo voUser = AppUserShortVoAssembler.create(domUser);
			if (domUser.getMos() != null && domUser.getMos().getName() != null)
				voUser.setUserRealName(domUser.getMos().getName().toString());
			ret.add(voUser);
			
		}
		return ret.sort();
	}

	public AppRoleShortVoCollection listRoles() 
	{
		StringBuilder hql = new StringBuilder();
		/* TODO MSSQL case - hql.append(" from AppRole appR where appR.isActive = 1"); */
		hql.append(" from AppRole appR where appR.isActive = true");
		
		return AppRoleShortVoAssembler.createAppRoleShortVoCollectionFromAppRole(getDomainFactory().find(hql.toString())).sort();
	}

	public AppUserVo getAppUser(AppUserShortVo appUserVo)
	{
		DomainFactory factory = getDomainFactory();
		return AppUserVoAssembler.create((AppUser)factory.getDomainObject(AppUser.class, appUserVo.getID_AppUser()));
	}

	public UserEmailAccountVo getEmailData(AppUserRefVo appUser) 
	{
		IMSCriteria imsc=new IMSCriteria (AppUser.class,getDomainFactory());
		imsc.equal("id", appUser.getID_AppUser());
		List users = imsc.find();
		if (users.size()>0)
		{
			AppUser user= (AppUser) users.get(0);
			return UserEmailAccountVoAssembler.create(user.getEmailAccount());
		}
		else
			return null;
	}

	public UserDTOVo getDtoAppUserByName(String name) throws DomainInterfaceException
	{
		if (name == null)
			throw new CodingRuntimeException("name cannot be null in method getDtoAppUserByName");
		
		User user = (User) getDTOInstance(User.class);
		user.Filter.clear();
		user.Filter.User = name;
		
		Result res = user.get();
		if (res != null && res.getId() != DtoErrorCode.NO_DATA_FOUND)
			throw new DomainInterfaceException(res.getMessage());
		
		// wdev-9034
		if (user.DataCollection == null || user.DataCollection.count() == 0)
			return null;
		
		UserRecord rec = user.DataCollection.get(0);
		UserDTOVo voUser = new UserDTOVo();
		voUser.setUserName(name);
		
		try
		{
			voUser.setEffectiveFrom( new DateTime(rec.Dateauthfr));
			voUser.setEffectiveTo(new DateTime(rec.Dateauthto));
		}
		catch (ParseException e1)
		{
			LOG.warn("DateTime Parsing Exception");
		}
		
		try{voUser.setPasswordExpiryDate(new Date().addDay(Integer.valueOf(rec.Daysleft)));}
		catch (NumberFormatException e){ LOG.warn("Password Expiry(days left) from PAS is invalid");}
		
		return voUser;
	}
}
