/*
 * Source: https://raw.githubusercontent.com/sushain97/contestManagement/master/src/util/RequestPrinter.java
 * Note: Slight modifications made to fit custom requirements.
 */

package by.stub.utils;

import by.stub.common.Common;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static by.stub.utils.FileUtils.BR;

public final class HttpRequestUtils {

   private static final String INDENT_UNIT = "   ";
   private static final String LEFT_BRACKET = "[";
   private static final String RIGHT_BRACKET = "]";
   private static final String LEFT_CURLY_BRACE = "{";
   private static final String RIGHT_CURLY_BRACE = "}";
   private static final String COMMA = ",";
   private static final String EMPTY_BRACES = LEFT_CURLY_BRACE + RIGHT_CURLY_BRACE;

   private HttpRequestUtils() {

   }

   private static String debugStringParameter(final String indentString, final String parameterName, final String[] parameterValues) {
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.
         append(indentString).
         append(INDENT_UNIT).
         append("'").append(parameterName).append("': ");
      if (parameterValues == null || parameterValues.length == 0) {
         stringBuilder.append("None");
      } else {
         if (parameterValues.length > 1) {
            stringBuilder.append(LEFT_BRACKET);
         }
         stringBuilder.append(StringUtils.join(parameterValues, COMMA.charAt(0)));
         if (parameterValues.length > 1) {
            stringBuilder.append(RIGHT_BRACKET);
         }
      }
      return stringBuilder.toString();
   }

   private static String debugStringHeader(final String indentString, final String headerName, final List<String> headerValues) {
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.
         append(indentString).
         append(INDENT_UNIT).
         append("'").append(headerName).append("': ");
      if (headerValues == null || headerValues.size() == 0) {
         stringBuilder.append("None");
      } else {
         if (headerValues.size() > 1) {
            stringBuilder.append(LEFT_BRACKET);
         }
         stringBuilder.append(StringUtils.join(headerValues.toArray(new String[headerValues.size()]), COMMA.charAt(0)));
         if (headerValues.size() > 1) {
            stringBuilder.append(RIGHT_BRACKET);
         }
      }
      return stringBuilder.toString();
   }

   private static String debugStringParameters(final HttpServletRequest request, final int indent) {
      final Enumeration<String> parameterNames = request.getParameterNames();
      if (parameterNames == null || !parameterNames.hasMoreElements()) {
         return EMPTY_BRACES;
      }

      final String indentString = StringUtils.repeat(INDENT_UNIT, indent);
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(LEFT_CURLY_BRACE).append(BR);
      while (parameterNames.hasMoreElements()) {
         final String parameterName = parameterNames.nextElement();
         final String[] parameterValues = request.getParameterValues(parameterName);
         stringBuilder.
            append(HttpRequestUtils.debugStringParameter(indentString, parameterName, parameterValues)).
            append(COMMA).append(BR);
      }
      stringBuilder.append(indentString).append(RIGHT_CURLY_BRACE).append(BR);
      return stringBuilder.toString();
   }

   private static String debugStringHeaders(final HttpServletRequest request, final int indent) {
      final Enumeration<String> headerNames = request.getHeaderNames();
      if (headerNames == null || !headerNames.hasMoreElements()) {
         return EMPTY_BRACES;
      }
      final String indentString = StringUtils.repeat(INDENT_UNIT, indent);
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(LEFT_CURLY_BRACE).append(BR);
      while (headerNames.hasMoreElements()) {
         final String headerName = headerNames.nextElement();
         final Enumeration<String> headerValues = request.getHeaders(headerName);
         final List<String> headerValuesList = new ArrayList<>();
         while (headerValues != null && headerValues.hasMoreElements()) {
            headerValuesList.add(headerValues.nextElement());
         }
         stringBuilder.
            append(HttpRequestUtils.debugStringHeader(indentString, headerName, headerValuesList)).
            append(COMMA).append(BR);
      }
      stringBuilder.append(indentString).append(RIGHT_CURLY_BRACE);
      return stringBuilder.toString();
   }

   /**
    * Debug complete request
    *
    * @param request Request parameter.
    * @return A string with debug information on Request's header
    */
   public static String dump(HttpServletRequest request) {

      //TODO If POST or PUT, HandlerUtils.extractPostRequestBody, MUST copy stream!!!

      final StringBuilder stringBuilder = new StringBuilder();

      stringBuilder.append(INDENT_UNIT + "PROTOCOL: ").append(request.getProtocol()).append(BR);
      stringBuilder.append(INDENT_UNIT + "METHOD: ").append(request.getMethod()).append(BR);
      stringBuilder.append(INDENT_UNIT + "CONTEXT PATH: ").append(request.getContextPath()).append(BR);
      stringBuilder.append(INDENT_UNIT + "SERVLET PATH: ").append(request.getServletPath()).append(BR);
      stringBuilder.append(INDENT_UNIT + "AUTH TYPE: ").append(request.getAuthType()).append(BR);
      stringBuilder.append(INDENT_UNIT + "REMOTE USER: ").append(request.getRemoteUser()).append(BR);
      stringBuilder.append(INDENT_UNIT + "REQUEST URI: ").append(request.getRequestURI()).append(BR);
      stringBuilder.append(INDENT_UNIT + "REQUEST URL: ").append(request.getRequestURL()).append(BR);
      stringBuilder.append(INDENT_UNIT + "QUERY STRING: ").append(request.getQueryString()).append(BR);
      stringBuilder.append(INDENT_UNIT + "PATH INFO: ").append(request.getPathInfo()).append(BR);
      stringBuilder.append(INDENT_UNIT + "PATH TRANSLATED: ").append(request.getPathTranslated()).append(BR);

      if (!Common.POSTING_METHODS.contains(request.getMethod().toUpperCase())) {
         stringBuilder.append(INDENT_UNIT + "PARAMETERS: ").append(HttpRequestUtils.debugStringParameters(request, 1)).append(BR);
      }

      stringBuilder.append(INDENT_UNIT + "HEADERS: ").append(HttpRequestUtils.debugStringHeaders(request, 1));

      return stringBuilder.toString();
   }
}
