/*
 * Copyright 2018, OpenCensus Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opencensus.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import io.opencensus.trace.MessageEvent;

/**
 * Helper class to convert/cast between for {@link MessageEvent} and {@link NetworkEvent}.
 *
 * @since 0.12
 */
@SuppressWarnings("deprecation")
public final class BaseMessageEventUtil {
  /**
   * Cast or convert a {@link io.opencensus.trace.BaseMessageEvent} to {@link MessageEvent}.
   *
   * <p>Warning: if the input is a {@code io.opencensus.trace.NetworkEvent} and contains {@code
   * kernelTimestamp} information, this information will be dropped.
   *
   * @param event the {@code BaseMessageEvent} that is being cast or converted.
   * @return a {@code MessageEvent} representation of the input.
   */
  public static MessageEvent asMessageEvent(io.opencensus.trace.BaseMessageEvent event) {
    checkNotNull(event);
    if (event instanceof MessageEvent) {
      return (MessageEvent) event;
    }
    io.opencensus.trace.NetworkEvent networkEvent = (io.opencensus.trace.NetworkEvent) event;
    MessageEvent.Type type =
        (networkEvent.getType() == io.opencensus.trace.NetworkEvent.Type.RECV)
            ? MessageEvent.Type.RECEIVED
            : MessageEvent.Type.SENT;
    return MessageEvent.builder(type, networkEvent.getMessageId())
        .setUncompressedMessageSize(networkEvent.getUncompressedMessageSize())
        .setCompressedMessageSize(networkEvent.getCompressedMessageSize())
        .build();
  }

  /**
   * Cast or convert a {@link io.opencensus.trace.BaseMessageEvent} to {@link
   * io.opencensus.trace.NetworkEvent}.
   *
   * @param event the {@code BaseMessageEvent} that is being cast or converted.
   * @return a {@code io.opencensus.trace.NetworkEvent} representation of the input.
   */
  public static io.opencensus.trace.NetworkEvent asNetworkEvent(
      io.opencensus.trace.BaseMessageEvent event) {
    checkNotNull(event);
    if (event instanceof io.opencensus.trace.NetworkEvent) {
      return (io.opencensus.trace.NetworkEvent) event;
    }
    MessageEvent messageEvent = (MessageEvent) event;
    io.opencensus.trace.NetworkEvent.Type type =
        (messageEvent.getType() == MessageEvent.Type.RECEIVED)
            ? io.opencensus.trace.NetworkEvent.Type.RECV
            : io.opencensus.trace.NetworkEvent.Type.SENT;
    return io.opencensus.trace.NetworkEvent.builder(type, messageEvent.getMessageId())
        .setUncompressedMessageSize(messageEvent.getUncompressedMessageSize())
        .setCompressedMessageSize(messageEvent.getCompressedMessageSize())
        .build();
  }

  private BaseMessageEventUtil() {}
}
