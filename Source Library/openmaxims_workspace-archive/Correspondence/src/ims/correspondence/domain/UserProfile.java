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

package ims.correspondence.domain;

// Generated from form domain impl
public interface UserProfile extends ims.domain.DomainInterface
{
	// Generated from form domain interface definition
	/**
	* list all medics
	*/
	public ims.core.vo.MedicLiteVoCollection listConsultants();

	// Generated from form domain interface definition
	public ims.core.vo.ClinicVoCollection listClinics();

	// Generated from form domain interface definition
	/**
	* List members of staff for a given wildcard username name.
	*/
	public ims.core.vo.MemberOfStaffShortVoCollection listMembrsOfStaff(ims.core.vo.MemberOfStaffShortVo filter);

	// Generated from form domain interface definition
	public ims.correspondence.vo.UserAccessVo saveUserAccess(ims.correspondence.vo.UserAccessVo voCollUserAccess) throws ims.domain.exceptions.StaleObjectException, ims.domain.exceptions.ForeignKeyViolationException, ims.domain.exceptions.UniqueKeyViolationException;

	// Generated from form domain interface definition
	public ims.admin.vo.AppUserShortVoCollection listAppUsers(ims.admin.vo.AppUserShortVo voAppUser);

	// Generated from form domain interface definition
	public ims.correspondence.vo.UserAccessVo getUserAccessForUser(Integer appUserID);

	// Generated from form domain interface definition
	public ims.correspondence.vo.UserAccessFullVo getFullUserAccessForUser(Integer appUserID);

	// Generated from form domain interface definition
	public ims.vo.interfaces.ICspTypeSpecifier[] listSpecifiers(ims.correspondence.vo.lookups.ProfileType profileType);
}