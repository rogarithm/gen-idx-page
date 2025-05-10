#!/usr/bin/env ruby

class HookReport
  attr_accessor :status, :msgs

  def initialize(status, msgs)
    self.status = status
    self.msgs = msgs
  end

  def to_s
    status << "\n" << msgs.join("\n")
  end
end
