package com.jayam.impactapp.common;

import java.util.Comparator;

import com.jayam.impactapp.objects.RegularDemandsDO;

public class RecieptComparator  implements Comparator<RegularDemandsDO>
{

	@Override
	public int compare(RegularDemandsDO lhs, RegularDemandsDO rhs) 
	{
		int rcptOne = Integer.parseInt(lhs.ReciptNumber.split("-")[4]);
		int rcptTwo = Integer.parseInt(rhs.ReciptNumber.split("-")[4]);
		
		if(rcptOne > rcptTwo)
			return 1;
		else
			return -1;
	}

}
