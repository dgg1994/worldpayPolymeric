package com.polymeric.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 货币转换工具类 基于 Frankfurter API，完全免费，无需API密钥 支持美元(USD)转任意货币
 */
public class CurrencyConverterUtil {

	// API地址
	private static final String BASE_API_URL = "https://api.frankfurter.app";
	private static final String API_URL = BASE_API_URL + "/latest?from=USD";

	// HTTP客户端（单例模式，复用连接）
	private static final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS).build();

	// JSON解析器
	private static final ObjectMapper objectMapper = new ObjectMapper();

	// 汇率缓存（可选，避免频繁请求API）
	private static volatile Map<String, BigDecimal> rateCache = null;
	private static final long CACHE_DURATION_MS = 30 * 60 * 1000; // 缓存30分钟
	private static volatile long lastCacheTime = 0;
	private static volatile String cacheDate = null; // 缓存数据的日期

	/**
	 * 汇率走势信息类
	 */
	public static class RateTrendInfo {
		private final String currency;           // 货币代码
		private final BigDecimal currentRate;     // 当前汇率
		private final BigDecimal previousRate;    // 对比汇率
		private final BigDecimal changeValue;     // 变化值
		private final BigDecimal changePercent;   // 变化百分比
		private final boolean isUp;               // 是否上涨
		private final String compareDate;         // 对比日期
		private final String currentDate;         // 当前日期

		public RateTrendInfo(String currency, BigDecimal currentRate, BigDecimal previousRate, 
							String compareDate, String currentDate) {
			this.currency = currency;
			this.currentRate = currentRate;
			this.previousRate = previousRate;
			this.compareDate = compareDate;
			this.currentDate = currentDate;

			if (previousRate != null && previousRate.compareTo(BigDecimal.ZERO) != 0) {
				this.changeValue = currentRate.subtract(previousRate);
				this.changePercent = changeValue.divide(previousRate, 4, RoundingMode.HALF_UP)
						.multiply(new BigDecimal("100"))
						.setScale(2, RoundingMode.HALF_UP);
				this.isUp = changeValue.compareTo(BigDecimal.ZERO) > 0;
			} else {
				this.changeValue = BigDecimal.ZERO;
				this.changePercent = BigDecimal.ZERO;
				this.isUp = false;
			}
		}

		public String getCurrency() { return currency; }
		public BigDecimal getCurrentRate() { return currentRate; }
		public BigDecimal getPreviousRate() { return previousRate; }
		public BigDecimal getChangeValue() { return changeValue; }
		public BigDecimal getChangePercent() { return changePercent; }
		public boolean isUp() { return isUp; }
		public String getCompareDate() { return compareDate; }
		public String getCurrentDate() { return currentDate; }

		/**
		 * 格式化涨跌幅显示（如 "+1.2%" 或 "-0.5%"）
		 */
		public String formatChangePercent() {
			String symbol = isUp ? "+" : "";
			return symbol + changePercent + "%";
		}

		/**
		 * 格式化涨跌幅显示（带箭头，如 "↑ +1.2%" 或 "↓ -0.5%"）
		 */
		public String formatChangeWithArrow() {
			String arrow = isUp ? "↑" : "↓";
			String symbol = isUp ? "+" : "";
			return String.format("%s %s%.2f%%", arrow, symbol, changePercent);
		}

		/**
		 * 格式化完整的汇率走势信息（类似图片中的格式）
		 */
		public String formatTrendInfo() {
			return String.format("当前汇率：1 USD = %.2f %s | %s",
					currentRate, currency, formatChangeWithArrow());
		}

		@Override
		public String toString() {
			return String.format("1 USD = %.4f %s (%s，对比 %s，涨跌: %s)",
					currentRate, currency, currentDate, compareDate, formatChangePercent());
		}
	}

	/**
	 * 获取所有汇率（带缓存）
	 */
	private static Map<String, BigDecimal> getRates() throws Exception {
		long now = System.currentTimeMillis();

		// 检查缓存是否有效
		if (rateCache != null && (now - lastCacheTime) < CACHE_DURATION_MS) {
			return rateCache;
		}

		// 请求最新汇率
		Request request = new Request.Builder().url(API_URL).addHeader("User-Agent", "Java Currency Converter").build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new RuntimeException("HTTP请求失败，状态码: " + response.code());
			}

			String jsonResponse = response.body().string();
			JsonNode rootNode = objectMapper.readTree(jsonResponse);

			// 获取当前日期
			cacheDate = rootNode.get("date").asText();

			// 解析汇率
			JsonNode ratesNode = rootNode.get("rates");
			if (ratesNode == null) {
				throw new RuntimeException("响应中没有找到 rates 字段");
			}

			rateCache = new HashMap<>();
			ratesNode.fields().forEachRemaining(entry -> {
				rateCache.put(entry.getKey(), entry.getValue().decimalValue());
			});

			lastCacheTime = now;
			return rateCache;
		}
	}

	/**
	 * 获取历史汇率
	 * 
	 * @param date 历史日期（格式：yyyy-MM-dd）
	 * @param targetCurrency 目标货币
	 * @return 历史汇率值，如果获取失败返回null
	 */
	private static BigDecimal getHistoricalRate(String date, String targetCurrency) throws Exception {
		String url = String.format("%s/%s?from=USD&to=%s", BASE_API_URL, date, targetCurrency);
		
		Request request = new Request.Builder()
				.url(url)
				.addHeader("User-Agent", "Java Currency Converter")
				.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				return null;
			}
			
			String jsonResponse = response.body().string();
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			
			JsonNode ratesNode = rootNode.get("rates");
			if (ratesNode != null && ratesNode.has(targetCurrency)) {
				return ratesNode.get(targetCurrency).decimalValue();
			}
			return null;
		}
	}

	/**
	 * 获取昨天的日期
	 */
	private static String getYesterdayDate() {
		return LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	/**
	 * 获取一周前的日期
	 */
	private static String getLastWeekDate() {
		return LocalDate.now().minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	/**
	 * 获取一个月前的日期
	 */
	private static String getLastMonthDate() {
		return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	/**
	 * 核心转换方法：将USD金额转换为目标货币
	 * 
	 * @param usdAmount      USD金额
	 * @param targetCurrency 目标货币代码（如 "TRY", "JPY", "EUR"）
	 * @return 转换后的金额
	 */
	private static BigDecimal convertInternal(BigDecimal usdAmount, String targetCurrency) throws Exception {
		// 获取最新汇率
		Map<String, BigDecimal> rates = getRates();

		// 检查目标货币是否支持
		if (!rates.containsKey(targetCurrency)) {
			throw new RuntimeException("不支持的货币代码: " + targetCurrency);
		}

		// 获取汇率
		BigDecimal exchangeRate = rates.get(targetCurrency);

		// 计算转换金额
		return usdAmount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * 方法1：根据USD金额和目标货币，返回转换后的金额（包含货币符号，保留两位小数） 例如：convertWithCurrency(4018.08,
	 * "TRY") 返回 "438849.00 TRY"
	 * 
	 * @param usdAmount      USD金额
	 * @param targetCurrency 目标货币代码（如 "TRY"）
	 * @return 转换后的金额字符串（如 "438849.00 TRY"），如果失败返回错误信息
	 */
	public static String convertWithCurrency(double usdAmount, String targetCurrency) {
		try {
			// 参数校验
			if (targetCurrency == null || targetCurrency.trim().isEmpty()) {
				return "目标货币代码不能为空";
			}

			String currency = targetCurrency.trim().toUpperCase();
			BigDecimal amount = BigDecimal.valueOf(usdAmount);

			// 执行转换
			BigDecimal convertedAmount = convertInternal(amount, currency);

			// 格式化为：保留两位小数 + 货币代码
			String formattedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP).toString();

			return formattedAmount + " " + currency;

		} catch (Exception e) {
			return "转换失败: " + e.getMessage();
		}
	}

	/**
	 * 方法2：根据目标货币，返回1 USD对应的汇率字符串（保留两位小数） 例如：getExchangeRateString("TRY") 返回 "1 USD
	 * = 340990.00 TRY"
	 * 
	 * @param targetCurrency 目标货币代码（如 "TRY"）
	 * @return 汇率字符串（如 "1 USD = 340990.00 TRY"），如果失败返回错误信息
	 */
	public static String getExchangeRateString(String targetCurrency) {
		try {
			// 参数校验
			if (targetCurrency == null || targetCurrency.trim().isEmpty()) {
				return "目标货币代码不能为空";
			}

			String currency = targetCurrency.trim().toUpperCase();

			// 获取汇率
			Map<String, BigDecimal> rates = getRates();
			BigDecimal rate = rates.get(currency);

			if (rate == null) {
				return "不支持的货币代码: " + currency;
			}

			// 格式化为：保留两位小数
			String formattedRate = rate.setScale(2, RoundingMode.HALF_UP).toString();

			return "1 USD ≈ " + formattedRate + " " + currency;

		} catch (Exception e) {
			return "获取汇率失败: " + e.getMessage();
		}
	}

	// ==================== 新增：汇率走势相关方法 ====================

	/**
	 * 方法3：获取汇率的涨跌幅走势（对比昨天）
	 * 
	 * @param targetCurrency 目标货币代码（如 "TRY"）
	 * @return 走势信息对象，如果失败返回null
	 */
	public static RateTrendInfo getRateTrend(String targetCurrency) {
		return getRateTrend(targetCurrency, getYesterdayDate());
	}

	/**
	 * 方法4：获取汇率的涨跌幅走势（对比指定日期）
	 * 
	 * @param targetCurrency 目标货币代码（如 "TRY"）
	 * @param compareDate 对比日期（格式：yyyy-MM-dd）
	 * @return 走势信息对象，如果失败返回null
	 */
	public static RateTrendInfo getRateTrend(String targetCurrency, String compareDate) {
		try {
			if (targetCurrency == null || targetCurrency.trim().isEmpty()) {
				return null;
			}

			String currency = targetCurrency.trim().toUpperCase();

			// 获取当前汇率
			Map<String, BigDecimal> rates = getRates();
			BigDecimal currentRate = rates.get(currency);
			if (currentRate == null) {
				return null;
			}

			// 获取历史汇率
			BigDecimal historicalRate = getHistoricalRate(compareDate, currency);
			if (historicalRate == null) {
				return null;
			}

			return new RateTrendInfo(currency, currentRate, historicalRate, compareDate, cacheDate);

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 方法5：获取汇率走势字符串（对比昨天）
	 * 例如：getRateTrendString("TRY") 返回 "+1.2%"
	 * 
	 * @param targetCurrency 目标货币代码
	 * @return 涨跌幅字符串（如 "+1.2%"），如果失败返回 "--%"
	 */
	public static String getRateTrendString(String targetCurrency) {
		RateTrendInfo trend = getRateTrend(targetCurrency);
		if (trend != null) {
			return trend.formatChangePercent();
		}
		return "--%";
	}

	/**
	 * 方法6：获取汇率走势字符串（带箭头，对比昨天）
	 * 例如：getRateTrendWithArrow("TRY") 返回 "↑ +1.2%"
	 * 
	 * @param targetCurrency 目标货币代码
	 * @return 带箭头的涨跌幅字符串（如 "↑ +1.2%"），如果失败返回 "--%"
	 */
	public static String getRateTrendWithArrow(String targetCurrency) {
		RateTrendInfo trend = getRateTrend(targetCurrency);
		if (trend != null) {
			return trend.formatChangeWithArrow();
		}
		return "--%";
	}

	/**
	 * 方法7：获取完整的汇率信息字符串（包含当前汇率和涨跌幅）
	 * 例如：getFullRateInfo("TRY") 返回 "当前汇率：1 USD = 35.45 TRY | ↑ +1.2%"
	 * 
	 * @param targetCurrency 目标货币代码
	 * @return 完整的汇率信息字符串
	 */
	public static String getFullRateInfo(String targetCurrency) {
		try {
			String currency = targetCurrency.trim().toUpperCase();
			Map<String, BigDecimal> rates = getRates();
			BigDecimal rate = rates.get(currency);
			
			if (rate == null) {
				return "不支持的货币代码: " + currency;
			}
			
			String formattedRate = rate.setScale(2, RoundingMode.HALF_UP).toString();
			String trendStr = getRateTrendWithArrow(currency);
			
			return String.format("当前汇率：1 USD = %s %s | %s", formattedRate, currency, trendStr);
			
		} catch (Exception e) {
			return "获取汇率信息失败: " + e.getMessage();
		}
	}

	/**
	 * 方法8：获取多日走势对比
	 * 例如：getMultiDayTrend("TRY", new String[]{"1d", "7d", "30d"})
	 * 
	 * @param targetCurrency 目标货币代码
	 * @param periods 对比周期数组（如 {"1d", "7d", "30d"}）
	 * @return 走势对比Map（周期 -> 涨跌幅百分比）
	 */
	public static Map<String, String> getMultiDayTrend(String targetCurrency, String[] periods) {
		Map<String, String> result = new HashMap<>();
		
		try {
			String currency = targetCurrency.trim().toUpperCase();
			Map<String, BigDecimal> rates = getRates();
			BigDecimal currentRate = rates.get(currency);
			
			if (currentRate == null) {
				return result;
			}
			
			for (String period : periods) {
				String compareDate = null;
				switch (period) {
					case "1d":
						compareDate = getYesterdayDate();
						break;
					case "7d":
						compareDate = getLastWeekDate();
						break;
					case "30d":
						compareDate = getLastMonthDate();
						break;
					default:
						continue;
				}
				
				BigDecimal historicalRate = getHistoricalRate(compareDate, currency);
				if (historicalRate != null) {
					BigDecimal changePercent = currentRate.subtract(historicalRate)
							.divide(historicalRate, 4, RoundingMode.HALF_UP)
							.multiply(new BigDecimal("100"))
							.setScale(2, RoundingMode.HALF_UP);
					String symbol = changePercent.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
					result.put(period, symbol + changePercent + "%");
				} else {
					result.put(period, "--%");
				}
			}
			
		} catch (Exception e) {
			// 返回空Map
		}
		
		return result;
	}

	/**
	 * 方法9：获取1 USD = X 目标货币的格式化字符串（带涨跌幅）
	 * 类似图片中的完整格式
	 * 
	 * @param usdAmount USD金额（用于显示）
	 * @param targetCurrency 目标货币代码
	 * @return 格式化后的完整字符串
	 */
	public static String getFormattedRateWithTrend(double usdAmount, String targetCurrency) {
		try {
			String currency = targetCurrency.trim().toUpperCase();
			Map<String, BigDecimal> rates = getRates();
			BigDecimal rate = rates.get(currency);
			
			if (rate == null) {
				return "不支持的货币代码: " + currency;
			}
			
			// 转换金额
			BigDecimal convertedAmount = BigDecimal.valueOf(usdAmount).multiply(rate)
					.setScale(2, RoundingMode.HALF_UP);
			
			// 获取涨跌幅
			RateTrendInfo trend = getRateTrend(currency);
			String trendStr = trend != null ? trend.formatChangeWithArrow() : "";
			
			// 构建输出（类似图片格式）
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("$ %,.2f", usdAmount)).append("\n\n");
			sb.append(String.format("当前汇率：1 USD = %.2f %s | %s", rate, currency, trendStr)).append("\n\n");
			sb.append(String.format("≈ %,.2f %s", convertedAmount, currency));
			
			return sb.toString();
			
		} catch (Exception e) {
			return "获取汇率信息失败: " + e.getMessage();
		}
	}

	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		System.out.println("========== 原有方法测试 ==========");
		
		// 测试方法1 - 转换金额
		String converted = convertWithCurrency(171, "CNY");
		System.out.println("方法1结果: " + converted);
		System.out.println();

		// 测试方法2 - 获取汇率字符串
		String rateInfo = getExchangeRateString("TRY");
		System.out.println("方法2结果: " + rateInfo);
		System.out.println();

	

		// 测试方法4 - 获取走势字符串
		String trendStr = getRateTrendString("TRY");
		System.out.println("方法4结果 (涨跌幅): " + trendStr);
		System.out.println();

	

		// 测试方法7 - 获取多日走势对比
		Map<String, String> multiDayTrend = getMultiDayTrend("TRY", new String[]{"1d", "7d", "30d"});
		System.out.println("方法7结果 (多日走势):");
		for (Map.Entry<String, String> entry : multiDayTrend.entrySet()) {
			System.out.println("  " + entry.getKey() + ": " + entry.getValue());
		}
		System.out.println();

	}
}