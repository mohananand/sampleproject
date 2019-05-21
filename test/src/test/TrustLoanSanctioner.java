package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrustLoanSanctioner {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		String FilePAth = "D:\\Gowtham\\Acadamy\\FeedFile.txt";
		String sanDate = "12/09/2013";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date sanDate1 = null;
		try {
			sanDate1 = sdf.parse(sanDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loanProcessor(FilePAth, sanDate1);

	}

	public static Map<Integer, Map<String, List<PolicyHolderVO>>> loanProcessor(
			String filePath, Date sanctionDate) {

		Map<Integer, Map<String, List<PolicyHolderVO>>> finalMap = new HashMap<Integer, Map<String, List<PolicyHolderVO>>>();

		TrustLoanSanctioner loan = new TrustLoanSanctioner();
		List<PolicyHolderVO> masterList = loan.readFile(filePath, sanctionDate);

		List<PolicyHolderVO> elgList = new ArrayList<PolicyHolderVO>();
		List<PolicyHolderVO> nelgList = new ArrayList<PolicyHolderVO>();
		List<PolicyHolderVO> dupList = new ArrayList<PolicyHolderVO>();
		List<PolicyHolderVO> invList = new ArrayList<PolicyHolderVO>();

		List<String> policyNumberList = new ArrayList<String>();

		Map<String, List<PolicyHolderVO>> firstMap = new HashMap<String, List<PolicyHolderVO>>();
		Map<String, List<PolicyHolderVO>> secondMap = new HashMap<String, List<PolicyHolderVO>>();

		String policyNumber = "";
		String[] elements = null;
		for (PolicyHolderVO policyVO : masterList) {
			if (mainValidation(policyVO)) {

				elements = policyVO.getPolicyCode().split("-");
				policyNumber = elements[1];

				if (!(policyNumberList.contains(policyNumber))) {
					String status = loanEligibility(policyVO.getPolicyCode(),
							policyVO.getReqLoanAmt(),
							policyVO.getAccPremiumAmt());
					if (status.equals("ELG")) {
						elgList.add(policyVO);
						policyNumberList.add(policyNumber);
					} else if (status.equals("NELG")) {
						nelgList.add(policyVO);
						policyNumberList.add(policyNumber);
					}

				} else {
					dupList.add(policyVO);
				}

				String[] no = policyVO.getPolicyCode().split("-");
				Float summAss = Float.parseFloat(no[2]);
				if (Float.parseFloat(policyVO.getReqLoanAmt()) > (summAss)) {
					invList.add(policyVO);
				}

				firstMap.put("ELG", elgList);
				firstMap.put("NELG", nelgList);
				secondMap.put("DUP", dupList);
				secondMap.put("INV", invList);

			}
		}

		finalMap.put(1, firstMap);
		finalMap.put(2, secondMap);
		System.out.println(finalMap);
		return finalMap;
	}

	public static boolean mainValidation(PolicyHolderVO vo) {
		boolean flag = false;
		if (validatePan(vo.getPan()) && validPolicyCode(vo.getPolicyCode())
				&& validAccamt(vo.getPolicyCode(), vo.getAccPremiumAmt())) {
			flag = true;
		}
		return flag;
	}

	public static boolean validatePan(String pan) {
		boolean flag = pan.startsWith("FR");
		char[] c = pan.toCharArray();
		if (pan.length() == 5 && flag && Character.isDigit(c[2])
				&& Character.isDigit(c[3]) && Character.isDigit(c[4])) {
			System.out.println("true");
			return true;
		} else
			System.out.println("false");
		return false;
	}

	public static boolean validPolicyCode(String policyCode) {
		String[] ele = policyCode.split("-");
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		if (ele.length == 3) {
			if (ele[0].equals("FST") || ele[0].equals("NRM")) {
				flag1 = true;
			}
			if (ele[1].matches("[1-9]+")) {
				flag2 = true;
			}
			if (ele[1].matches("[0-9]+")) {
				flag3 = true;
			}
		}
		if (flag1 && flag2 && flag3) {
			flag = true;
		}
		return flag;
	}

	public static boolean validAccamt(String code, String accAmt) {
		boolean flag = false;
		String[] ele = code.split("-");
		if (Integer.parseInt(accAmt) < Integer.parseInt(ele[2])) {
			flag = true;
		}
		return flag;
	}

	public int monthsInbetween(Date startDate, Date endDate) {
		Calendar scal = new GregorianCalendar();
		scal.setTime(startDate);
		Calendar ecal = new GregorianCalendar();
		ecal.setTime(endDate);
		int diffYear = ecal.get(Calendar.YEAR) - scal.get(Calendar.YEAR);
		int diffMonth = (diffYear * 12)
				+ (ecal.get(Calendar.MONTH) - scal.get(Calendar.MONTH));
		return diffMonth;
	}

	public void daysInbetween(Date sdate, Date edate) {
		int days = (int) ((edate.getTime() - sdate.getTime()) / (1000 * 24 * 60 * 60));
	}

	public static String loanEligibility(String code, String reqAmt,
			String accAmt) {
		String status = "";
		String ele[] = code.split("-");
		String assSum = ele[2];
		if (ele[0].equals("NRM")
				&& Integer.parseInt(reqAmt) > (40 * Integer.parseInt(accAmt) / 100)) {
			status = "NELG";
		} else if (ele[0].equals("NRM")
				&& Integer.parseInt(reqAmt) < (40 * Integer.parseInt(accAmt) / 100)) {
			status = "ELG";
		} else if (ele[0].equals("FST")
				&& Integer.parseInt(reqAmt) < (60 * Integer.parseInt(accAmt) / 100)) {
			status = "ELG";
		} else if (ele[0].equals("NRM")
				&& (Integer.parseInt(reqAmt) > (60 * Integer.parseInt(accAmt) / 100))
				&& (Integer.parseInt(reqAmt) < (70 * Integer.parseInt(assSum) / 100))) {
			status = "ELG";
		} else if (ele[0].equals("NRM")
				&& (Integer.parseInt(reqAmt) > (60 * Integer.parseInt(accAmt) / 100))
				&& (Integer.parseInt(reqAmt) > (70 * Integer.parseInt(assSum) / 100))) {
			status = "NELG";
		}

		return status;
	}

	public Float calNetPremiumAmt(String code, String period, String reqLoan,
			Date sanDate, Date startdate) {
		String[] ele = code.split("-");
		String sumAss = ele[2];
		Float calPreAmt = Float.parseFloat(sumAss) / Float.parseFloat(period);

		Date polEndDate = null;
		Calendar cal = new GregorianCalendar();
		cal.setTime(startdate);
		cal.add(Calendar.MONTH, Integer.parseInt(period));
		polEndDate = cal.getTime();
		int loanPeriod = monthsInbetween(sanDate, polEndDate);
		Float interest = (float) (((Float.parseFloat(reqLoan)) * 1.2 * loanPeriod) / 100);
		Float incpremium = (Float.parseFloat(reqLoan) * interest) / loanPeriod;
		Float netPermiumAmt = calPreAmt + incpremium;
		return netPermiumAmt;
	}

	public List<PolicyHolderVO> readFile(String filePath, Date sanDate) {
		String line = null;
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<PolicyHolderVO> masterList = new ArrayList<PolicyHolderVO>();
		boolean flag = false;
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			try {
				while ((line = br.readLine()) != null) {
					String[] elements = line.split(";");
					try {
						date = sdf.parse(elements[2]);
						flag = true;
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						flag = false;
					}
					if (elements.length == 6 && flag) {
						PolicyHolderVO policyVO = new PolicyHolderVO();
						policyVO.setPolicyCode(elements[0]);
						policyVO.setPan(elements[1]);
						policyVO.setStartDate(date);
						policyVO.setPeriod(elements[3]);
						policyVO.setAccPremiumAmt(elements[4]);
						policyVO.setReqLoanAmt(elements[5]);

						// policyVO.setNetPremAmt(calNetPremiumAmt(elements[0],
						// elements[3], elements[5], sanDate, date));
						masterList.add(policyVO);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return masterList;
	}
}