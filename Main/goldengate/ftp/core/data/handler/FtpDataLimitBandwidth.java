/**
 * 
 */
package goldengate.ftp.core.data.handler;

import goldengate.ftp.core.exception.FtpInvalidArgumentException;
import goldengate.ftp.core.utils.bandwidth.LimitBandwithHandler;
import goldengate.ftp.core.utils.bandwidth.ThroughputMonitor;

import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.MessageEvent;

/**
 * Channel Handler that allows to limit the global bandwidth
 * or per session bandwidth. This is to be placed between
 * the {@link FtpDataModeCodec} and the {@link FtpDataTypeCodec}
 * in order to have {@link FtpDataBlock} object.
 * @author fbregier
 *
 */
@ChannelPipelineCoverage("one")
public class FtpDataLimitBandwidth extends LimitBandwithHandler {
	
	/**
	 * @param globalMonitor
	 * @param sessionMonitor
	 */
	public FtpDataLimitBandwidth(ThroughputMonitor globalMonitor, ThroughputMonitor sessionMonitor) {
		super(globalMonitor, sessionMonitor);
	}
	/* (non-Javadoc)
	 * @see goldengate.ftp.core.utils.LimitBandwithHandler#getMessageSize(org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	protected long getMessageSize(MessageEvent arg1) throws Exception {
		Object o = arg1.getMessage();
		if (! (o instanceof FtpDataBlock)) {
			// Type unimplemented
			throw new FtpInvalidArgumentException("Wrong object received in "+this.getClass().getName()+" codec "+o.getClass().getName());
		}
		FtpDataBlock dataBlock = (FtpDataBlock) o;
		return dataBlock.getByteCount();
	}
}
