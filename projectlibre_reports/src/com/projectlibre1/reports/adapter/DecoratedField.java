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
package com.projectlibre1.reports.adapter;

import net.sf.jasperreports.engine.JRField;

import com.projectlibre1.configuration.Configuration;
import com.projectlibre1.field.Field;

/**
 *
 */
public class DecoratedField {
	/**
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName The fieldName to set.
	 */
	private void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return Returns the subField.
	 */
	public boolean isSubField() {
		return subField;
	}
	/**
	 * @param subField The subField to set.
	 */
	private void setSubField(boolean subField) {
		this.subField = subField;
	}
	/**
	 * @return Returns the textField.
	 */
	public boolean isTextField() {
		return textField;
	}
	/**
	 * @param textField The textField to set.
	 */
	private void setTextField(boolean textField) {
		this.textField = textField;
	}

	private String fieldName = "";
	private boolean textField = false;
	private boolean subField = false;
	private Class clazz = null;
	private String method = "";
	/**
	 * @return Returns the timeBased.
	 */
	public boolean isTimeBased() {
		return timeBased;
	}
	/**
	 * @param timeBased The timeBased to set.
	 */
	public void setTimeBased(boolean timeBased) {
		this.timeBased = timeBased;
	}
	private boolean timeBased = false;
	/**
	 * @return Returns the end.
	 */
	public long getEnd() {
		return end;
	}
	/**
	 * @param end The end to set.
	 */
	public void setEnd(long end) {
		this.end = end;
	}
	/**
	 * @return Returns the start.
	 */
	public long getStart() {
		return start;
	}
	/**
	 * @param start The start to set.
	 */
	public void setStart(long start) {
		this.start = start;
	}
	private long start = 0;
	private long end = 0;

	/**
	 * @return Returns the method.
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method The method to set.
	 */
	private void setMethod(String method) {
		this.method = method;
	}
	Field fieldForReportField() {
		return Configuration.getFieldFromId("Field." + fieldName);
	}
	/**
	 * @return Returns the clazz.
	 */
	public Class getClazz() {
		return clazz;
	}
	/**
	 * @param clazz The clazz to set.
	 */
	private void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	public DecoratedField(JRField jrField) {

		String name = jrField.getName();
		
		String option = "MODText";
		if (name.indexOf(option) >= 0) {
			setTextField(true);
		}

		option = "METHOD";
		if(name.indexOf(option) >= 0) {
			String method = name.substring(name.indexOf(option) + new String(option).length());
			method = method.substring(0, method.indexOf('_'));
			setMethod(method);
		}
		
		option = "FIELD";
		if(name.indexOf(option) >= 0) {
			fieldName = name.substring(name.indexOf(option) + new String(option).length());
		}
		else
		{
			fieldName = name;
		}
		
		setFieldName(fieldName);

		option = "TIME";
		if(name.indexOf(option) >= 0) {
			String timeString = name.substring(name.indexOf(option) + new String(option).length());
			String startString = timeString.substring(0, timeString.indexOf('_'));
			String endString = timeString.substring(timeString.indexOf('_') + 1);
//			System.out.println("time based field start " + startString + ", end " + endString);
			endString = endString.substring(0, endString.indexOf('_'));
//			System.out.println("time based field start " + startString + ", end " + endString);
			setTimeBased(true);
			setStart(new Long(startString).longValue());
			setEnd(new Long(endString).longValue());
//			System.out.println("time based field start " + getStart() + ", end " + getEnd());
		}
	}
}
