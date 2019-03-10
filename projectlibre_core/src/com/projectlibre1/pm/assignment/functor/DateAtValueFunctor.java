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
package com.projectlibre1.pm.assignment.functor;

import com.projectlibre1.algorithm.CalculationVisitor;
import com.projectlibre1.pm.time.HasStartAndEnd;

/**
 * Will determine the date when a value is achieved.  The process is rather tricky since there are fixed costs
 * to take into account.  This fuctor is used when calculating reverse queries.
 */
public class DateAtValueFunctor implements CalculationVisitor{
	AssignmentFieldClosureCollection childList;
	double subtotal;
	double value;
	long date;
	
	public static DateAtValueFunctor getInstance(double value, com.projectlibre1.pm.assignment.functor.AssignmentFieldClosureCollection childList) {
		return new DateAtValueFunctor(value, childList);
	}


	/**
	 * Constructor 
	 */
	private DateAtValueFunctor(double value, AssignmentFieldClosureCollection childList) {
		super();
		reset();
		this.value = value;
		this.childList = childList;
	}
	
	
	/* (non-Javadoc)
	 * @see com.projectlibre1.algorithm.CalculationVisitor#reset()
	 */
	public void initialize() {
		// no need to reset anything
	}
	
	public void reset() {
		subtotal = 0;
		value = 0;
		date = 0;
	}
	/**
	 * Increment the subtotal by adding up all child functors.  If the value is achieved, calculate the
	 * instant in the range at which it occurs.
	 */
	public void execute(Object object) {
		HasStartAndEnd interval = (HasStartAndEnd) object;

		double sum = childList.getValue();
		subtotal += sum;
//System.out.println("subtotal is " + DurationFormat.format((long)subtotal) + "interval is " + new java.util.Date(interval.getStart()) + new java.util.Date(interval.getEnd())); 	
		if (date == 0 && subtotal >= value) {
			if (value == 0.0) { // take care of degenerate case
				date = interval.getStart();
				return;
			}
			// if just an instant but the instant has costs that put it over the top, return the instant
			if (interval.getStart() == interval.getEnd()) {
				date = interval.getStart();
				return;
			}
  				
			double fixedSum = childList.getFixedValue(); // get fixed only
			if (subtotal + fixedSum - sum >= value) { // if the fixed cost alone puts it over
				date = interval.getStart();
				return;
			}

			// figure out the date using a prorated amount of variable cost
			sum -= fixedSum; // remove any fixed sum
			double fractionOfDuration = (sum - (subtotal - value)) / sum;
			
			AssignmentFieldFunctor aNonZeroFunctor = childList.getANonZeroFunctor();
			
			long duration = aNonZeroFunctor.getWorkCalendar().compare(interval.getEnd(),interval.getStart(), false);			
			date = aNonZeroFunctor.getWorkCalendar().add(interval.getStart(),(long) (duration * fractionOfDuration),true);
		}
		
	}
	/**
	 * Get the date which the value occurs.  Will be 0 if it never occurs
	 */
	public long getDate() {
		return date;
	}


	/* (non-Javadoc)
	 * @see com.projectlibre1.algorithm.CalculationVisitor#isCumulative()
	 */
	public boolean isCumulative() {
		// TODO Auto-generated method stub
		return false;
	}
}
