package com.andreabaccega.formedittextvalidator;

import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Patterns;
import android.widget.EditText;
/**
 * 针对中国 11位手机号码
 * @author HungLau.Ngai
 *
 */
public class CellPhoneValidator extends PatternValidator{
	@SuppressLint("NewApi")
	public CellPhoneValidator(String _customErrorMessage) {

		super(_customErrorMessage, Build.VERSION.SDK_INT>=8?Patterns.PHONE:Pattern.compile(                                  // sdd = space, dot, or dash
				"(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
				+ "(\\([0-9]+\\)[\\- \\.]*)?"               // (<digits>)<sdd>*
				+ "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])"));
	}

	@Override
	public boolean isValid(EditText et) {
		boolean rsSu = super.isValid(et);
		if(rsSu)
			return et.getText().toString().length() == 11;
		return rsSu;
	}
	
	
	
}
