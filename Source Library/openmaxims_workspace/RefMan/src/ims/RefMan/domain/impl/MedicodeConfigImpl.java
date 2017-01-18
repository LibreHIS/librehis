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
// This code was generated by Florin Blindu using IMS Development Environment (version 1.80 build 5332.26009)
// Copyright (C) 1995-2014 IMS MAXIMS. All rights reserved.

package ims.RefMan.domain.impl;

import ims.RefMan.domain.base.impl.BaseMedicodeConfigImpl;
import ims.core.configuration.domain.objects.ExternalCodingConfig;
import ims.core.vo.ExternalCodingConfigVo;
import ims.core.vo.domain.ExternalCodingConfigVoAssembler;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.exceptions.UniqueKeyViolationException;
import ims.framework.exceptions.CodingRuntimeException;

import java.util.List;

public class MedicodeConfigImpl extends BaseMedicodeConfigImpl
{

	private static final long serialVersionUID = 1L;

	public ims.core.vo.ExternalCodingConfigVo getExternalCodingConfig()
	{
		DomainFactory factory = getDomainFactory();

		StringBuffer hql = new StringBuffer();

		hql.append("from ExternalCodingConfig");

		List<?> list = factory.find(hql.toString());

		if (list != null && list.size() > 0)
			return ExternalCodingConfigVoAssembler.create((ExternalCodingConfig) (list).get(0));

		return null;
	}

	public ExternalCodingConfigVo save(ExternalCodingConfigVo externalCodingConfigToSave) throws StaleObjectException, UniqueKeyViolationException
	{
		if (externalCodingConfigToSave == null )
			throw new CodingRuntimeException("Cannot save null ExternalCodingConfigVo ");

		if (!externalCodingConfigToSave.isValidated())
				throw new DomainRuntimeException("ExternalCodingConfigVo Not Validated.");

		DomainFactory factory = getDomainFactory();
		
		ExternalCodingConfig domainExternalCodingConfig = ExternalCodingConfigVoAssembler.extractExternalCodingConfig(factory, externalCodingConfigToSave);
		factory.save(domainExternalCodingConfig);

		return ExternalCodingConfigVoAssembler.create(domainExternalCodingConfig);
	}

	
}
