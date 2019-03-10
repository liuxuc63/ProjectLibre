/*******************************************************************************
 * The contents of this file are subject to the Common Public Attribution License 
 * Version 1.0 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.projectlibre.com/license . The License is based on the Mozilla Public 
 * License Version 1.1 but Sections 14 and 15 have been added to cover use of 
 * software over a computer network and provide for limited attribution for the 
 * Original Developer. In addition, Exhibit A has been modified to be consistent 
 * with Exhibit B. 
 *
 * Software distributed under the License is distributed on an "AS IS" basis, 
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the 
 * specific language governing rights and limitations under the License. The 
 * Original Code is ProjectLibre. The Original Developer is the Initial Developer 
 * and is ProjectLibre Inc. All portions of the code written by ProjectLibre are 
 * Copyright (c) 2012-2019. All Rights Reserved. All portions of the code written by 
 * ProjectLibre are Copyright (c) 2012-2019. All Rights Reserved. Contributor 
 * ProjectLibre, Inc.
 *
 * Alternatively, the contents of this file may be used under the terms of the 
 * ProjectLibre End-User License Agreement (the ProjectLibre License) in which case 
 * the provisions of the ProjectLibre License are applicable instead of those above. 
 * If you wish to allow use of your version of this file only under the terms of the 
 * ProjectLibre License and not to allow others to use your version of this file 
 * under the CPAL, indicate your decision by deleting the provisions above and 
 * replace them with the notice and other provisions required by the ProjectLibre 
 * License. If you do not delete the provisions above, a recipient may use your 
 * version of this file under either the CPAL or the ProjectLibre Licenses. 
 *
 *
 * [NOTE: The text of this Exhibit A may differ slightly from the text of the notices 
 * in the Source Code files of the Original Code. You should use the text of this 
 * Exhibit A rather than the text found in the Original Code Source Code for Your 
 * Modifications.] 
 *
 * EXHIBIT B. Attribution Information for ProjectLibre required
 *
 * Attribution Copyright Notice: Copyright (c) 2012-2019, ProjectLibre, Inc.
 * Attribution Phrase (not exceeding 10 words): 
 * ProjectLibre, open source project management software.
 * Attribution URL: http://www.projectlibre.com
 * Graphic Image as provided in the Covered Code as file: projectlibre-logo.png with 
 * alternatives listed on http://www.projectlibre.com/logo 
 *
 * Display of Attribution Information is required in Larger Works which are defined 
 * in the CPAL as a work which combines Covered Code or portions thereof with code 
 * not governed by the terms of the CPAL. However, in addition to the other notice 
 * obligations, all copies of the Covered Code in Executable and Source Code form 
 * distributed must, as a form of attribution of the original author, include on 
 * each user interface screen the "ProjectLibre" logo visible to all users. 
 * The ProjectLibre logo should be located horizontally aligned with the menu bar 
 * and left justified on the top left of the screen adjacent to the File menu. The 
 * logo must be at least 144 x 31 pixels. When users click on the "ProjectLibre" 
 * logo it must direct them back to http://www.projectlibre.com. 
 *******************************************************************************/
package com.projectlibre.core.pm.exchange.converters.mpx;

import com.projectlibre.core.pm.exchange.converters.mpx.type.MpxDurationConverter;
import com.projectlibre.core.pm.exchange.converters.type.CalendarUTCLongConverter;
import com.projectlibre.core.time.DefaultTimephasedValue;
import com.projectlibre.core.time.Duration;
import com.projectlibre.core.time.TimeUnit;
import com.projectlibre.core.time.TimephasedType;
import com.projectlibre.core.time.TimephasedValue;

import net.sf.mpxj.mspdi.DatatypeConverter;

/**
 * @author Laurent Chretienneau
 *
 */
public class MpxTimephasedConverter {
	public TimephasedValue<?> from(net.sf.mpxj.mspdi.schema.TimephasedDataType mpxTimephased, MpxImportState state) {
		TimephasedType type=TimephasedType.getInstance(mpxTimephased.getType().intValue());
		if (!type.isWork())
			return null;
		CalendarUTCLongConverter dateConverter=new CalendarUTCLongConverter();
		long start=(Long)dateConverter.from(mpxTimephased.getStart());
		long finish=(Long)dateConverter.from(mpxTimephased.getFinish());
		
		Duration value;
		String rawValue=mpxTimephased.getValue();
		if (rawValue==null ||
				rawValue.length()==0 ||
				"0".equals(rawValue) ||
				"PT0H0M0S".equals(rawValue))
			value=new Duration(0.0D, TimeUnit.HOURS);
		else if ("PT8H0M0S".equals(rawValue))
			value=new Duration(8*3600000D, TimeUnit.HOURS);
		else{
			MpxDurationConverter durationConverter=new MpxDurationConverter();
			net.sf.mpxj.Duration mpxDuration=DatatypeConverter.parseDuration(state.getMpxProjectFile(),null,rawValue);
			value=(Duration)durationConverter.from(mpxDuration);
		}
		TimephasedValue<Duration> timephased=new DefaultTimephasedValue<Duration>(start,finish,value,type);
		return timephased;
	}

	
}
