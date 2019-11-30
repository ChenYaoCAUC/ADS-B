package util;

public enum ReasonEnum {
	SUCCESS("�ɹ�"),
	UNKNOWN("δ֪����"),
	FILE_UPLOAD("�ϴ��ļ�ʧ��"),
	NO_SUCH_PYTHON_METHOD("û���Ǹ���⹦��"),
	NO_SUCH_RISK("û���Ǹ�����"),
	PYTHON_IO("���������ͨ��ʱ����"),
	PYTHON_RUNTIME("��������������һ������"),
	PORT_RUN_OUT("�Ѵﵽ���ͻ���������"),
	CLIENT_ALREADY_EXISTS("�ͻ�����ע���ӳ��˿�"),
	NO_SUCH_CLINET("û���Ǹ��ͻ���"),
	INVALID_PARAM("��������"),
	FRIDA_ERROR("Զ�̵��Է�����������һ������");
	
	private String reason;
	
	private ReasonEnum(String reason) {
		this.reason = reason;
	}
	
	public String get() {
		return reason;
	}
}
